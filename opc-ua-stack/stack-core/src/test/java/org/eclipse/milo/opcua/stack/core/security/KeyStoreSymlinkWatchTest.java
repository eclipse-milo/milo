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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class KeyStoreSymlinkWatchTest {

  @TempDir Path directory;

  // The configured symlink and its real target are independent rotation points. A shared parent
  // means both registrations use the same WatchKey, which must survive a later link retarget.
  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  void watchesTargetRotationsAndLinkRetargeting(boolean sameDirectory) throws Exception {
    Path config = Files.createDirectory(directory.resolve("config"));
    Path firstDirectory =
        sameDirectory ? config : Files.createDirectory(directory.resolve("first"));
    Path secondDirectory = Files.createDirectory(directory.resolve("second"));
    Path firstTarget = firstDirectory.resolve("first.pfx");
    Path secondTarget = secondDirectory.resolve("second.pfx");
    Path link = config.resolve("identity.pfx");
    var factory = new TestCertificateFactory();
    var keyPair = factory.createRsaSha256KeyPair();
    var entry =
        new CertificateStore.Entry(
            keyPair.getPrivate(), factory.createRsaSha256CertificateChain(keyPair));
    NodeId first = new NodeId(2, "first");
    NodeId second = new NodeId(2, "second");
    NodeId rotated = new NodeId(2, "rotated");

    try (var firstWriter = new KeyStoreCertificateStore(settings(firstTarget, false));
        var secondWriter = new KeyStoreCertificateStore(settings(secondTarget, false))) {
      firstWriter.initialize();
      secondWriter.initialize();
      secondWriter.set(second, entry);
      try {
        Files.createSymbolicLink(link, firstTarget);
      } catch (IOException | UnsupportedOperationException e) {
        assumeTrue(false, "symbolic links unavailable: " + e);
      }
      try (var watcher = new ObservingStore(settings(link, true))) {
        watcher.initialize();
        assertFalse(watcher.contains(first));
        CompletableFuture<Void> changed = watcher.expect(first);
        firstWriter.set(first, entry);
        changed.get(30, TimeUnit.SECONDS);
        assertNotNull(watcher.get(first));

        changed = watcher.expect(second);
        retarget(link, secondTarget);
        changed.get(30, TimeUnit.SECONDS);
        assertFalse(watcher.contains(first), "retargeting must replace the cached KeyStore");
        changed = watcher.expect(rotated);
        secondWriter.set(rotated, entry);
        changed.get(30, TimeUnit.SECONDS);
        assertNotNull(watcher.get(rotated));

        // Returning to the original directory proves the configured-link watch was not cancelled
        // when its original target shared the same WatchKey.
        changed = watcher.expect(first);
        retarget(link, firstTarget);
        changed.get(30, TimeUnit.SECONDS);
        assertTrue(watcher.contains(first));
        assertFalse(watcher.contains(second));
      }
    }
  }

  private static void retarget(Path link, Path target) throws IOException {
    Path replacement = link.resolveSibling("replacement-link");
    Files.createSymbolicLink(replacement, target);
    Files.move(
        replacement, link, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
  }

  private static KeyStoreCertificateStore.Settings settings(Path path, boolean watch) {
    return new KeyStoreCertificateStore.Settings(
        path, "password"::toCharArray, alias -> "password".toCharArray(), watch);
  }

  private static final class ObservingStore extends KeyStoreCertificateStore {
    private final AtomicReference<ExpectedEntry> expected = new AtomicReference<>();

    ObservingStore(Settings settings) {
      super(settings);
    }

    CompletableFuture<Void> expect(NodeId typeId) {
      var changed = new CompletableFuture<Void>();
      expected.set(new ExpectedEntry(typeId, changed));
      return changed;
    }

    @Override
    void reload(Path path) {
      super.reload(path);
      ExpectedEntry entry = expected.get();
      if (entry != null) {
        try {
          if (contains(entry.typeId())) {
            entry.changed().complete(null);
          }
        } catch (Exception e) {
          entry.changed().completeExceptionally(e);
        }
      }
    }
  }

  private record ExpectedEntry(NodeId typeId, CompletableFuture<Void> changed) {}
}
