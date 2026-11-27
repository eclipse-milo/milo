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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.security.KeyPair;
import java.security.KeyStore;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class KeyStoreSymlinkTest {

  @TempDir Path directory;

  @Nested
  class Initialize {

    // An application may start before the volume backing a configured symlink is mounted.
    // initialize() must fail loudly with an error naming both the link and resolved target,
    // leave the dangling symlink intact, and remain retryable once the directory appears.
    @Test
    void initializeFailsAndPreservesDanglingLinkWhenTargetDirectoryIsMissing() throws Exception {
      Path root = directory.toRealPath();
      Path configDir = Files.createDirectory(root.resolve("config"));
      Path missingDir = root.resolve("unmounted-volume");
      Path target = missingDir.resolve("application.pfx");
      Path link = configDir.resolve("identity.pfx");

      createSymbolicLink(link, target);

      try (var store = new KeyStoreCertificateStore(settings(link, false))) {
        NoSuchFileException error = assertThrows(NoSuchFileException.class, store::initialize);
        assertTrue(
            error.getMessage().contains(link.toString()),
            "error message should name the configured symlink: " + error.getMessage());
        assertTrue(
            error.getMessage().contains(target.toString()),
            "error message should name the resolved target: " + error.getMessage());

        assertTrue(Files.isSymbolicLink(link), "dangling symlink must not be replaced");
        assertEquals(target, Files.readSymbolicLink(link));
        assertFalse(Files.exists(target, LinkOption.NOFOLLOW_LINKS));

        // Once the backing volume is mounted, retrying initialize() on the same store succeeds.
        Files.createDirectory(missingDir);
        store.initialize();

        assertTrue(Files.isSymbolicLink(link), "symlink must remain intact after retry");
        assertTrue(Files.isRegularFile(target, LinkOption.NOFOLLOW_LINKS));
        assertNotNull(loadKeyStore(target));
      }
    }

    // Missing-parent detection must happen during path resolution before initialize() requests
    // passwords or prepares an empty KeyStore, so if a volume with an existing KeyStore is mounted
    // afterward, retrying initialize() loads that populated identity intact.
    @Test
    void initializeFailsBeforeReadingPasswordWhenTargetDirectoryIsMissingAndLoadsMountedStore()
        throws Exception {

      Path root = directory.toRealPath();
      Path configDir = Files.createDirectory(root.resolve("config"));
      Path missingDir = root.resolve("delayed-mount");
      Path target = missingDir.resolve("application.pfx");
      Path link = configDir.resolve("identity.pfx");

      createSymbolicLink(link, target);

      var passwordRequested = new AtomicBoolean(false);
      var delayedMountSettings =
          new KeyStoreCertificateStore.Settings(
              link,
              () -> {
                passwordRequested.set(true);
                return "password".toCharArray();
              },
              alias -> "password".toCharArray(),
              false);

      try (var store = new KeyStoreCertificateStore(delayedMountSettings)) {
        NoSuchFileException error = assertThrows(NoSuchFileException.class, store::initialize);
        assertTrue(error.getMessage().contains(link.toString()));
        assertTrue(error.getMessage().contains(target.toString()));
        assertFalse(
            passwordRequested.get(),
            "initialize() must fail during path resolution before requesting KeyStore password");

        // Simulate the volume mounting with a pre-existing populated KeyStore.
        Files.createDirectory(missingDir);
        NodeId mountedId = NodeIds.RsaSha256ApplicationCertificateType;
        CertificateStore.Entry mountedEntry = newEntry();
        try (var seed = KeyStoreCertificateStore.createAndInitialize(settings(target, false))) {
          seed.set(mountedId, mountedEntry);
        }

        store.initialize();

        assertTrue(passwordRequested.get());
        assertTrue(store.contains(mountedId));
        assertEquals(mountedEntry.certificateChain[0], store.get(mountedId).certificateChain[0]);
      }
    }

    // Operators frequently pre-create a symlink to where a new KeyStore will live before first
    // startup. initialize() must create the file at the link target (for both absolute and
    // relative links), preserve the symlink, and allow watchForChanges to observe target updates.
    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void initializeCreatesKeyStoreAtDanglingLinkTargetAndPreservesLink(boolean relative)
        throws Exception {

      Path root = directory.toRealPath();
      Path configDir = Files.createDirectory(root.resolve("config"));
      Path secretsDir = Files.createDirectory(root.resolve("secrets"));
      Path target = secretsDir.resolve("application.pfx");
      Path link = configDir.resolve("identity.pfx");
      Path linkTarget = relative ? configDir.relativize(target) : target;

      createSymbolicLink(link, linkTarget);

      try (var store = new KeyStoreCertificateStore(settings(link, true))) {
        store.initialize();

        assertTrue(Files.isSymbolicLink(link), "configured symlink must not be replaced");
        assertEquals(linkTarget, Files.readSymbolicLink(link));
        assertTrue(Files.isRegularFile(target, LinkOption.NOFOLLOW_LINKS));
        assertNotNull(loadKeyStore(target));

        // Watching must observe external writes to the newly created target file.
        NodeId watchedId = new NodeId(2, "watched-entry");
        try (var writer = KeyStoreCertificateStore.createAndInitialize(settings(target, false))) {
          writer.set(watchedId, newEntry());
        }
        assertTrue(
            awaitContains(store, watchedId),
            "watcher initialized from dangling symlink should reload updates to its target");
      }
    }

    // Chained symlinks (link1 -> link2 -> target) must resolve all the way to the final target
    // both when the target directory exists and when it is missing.
    @Test
    void initializeFollowsChainOfDanglingSymlinks() throws Exception {
      Path root = directory.toRealPath();
      Path dirA = Files.createDirectory(root.resolve("a"));
      Path dirB = Files.createDirectory(root.resolve("b"));
      Path missingDir = root.resolve("missing");
      Path missingTarget = missingDir.resolve("final.pfx");
      Path link2 = dirB.resolve("hop.pfx");
      Path link1 = dirA.resolve("entry.pfx");

      createSymbolicLink(link2, dirB.relativize(missingTarget));
      createSymbolicLink(link1, dirA.relativize(link2));

      try (var store = new KeyStoreCertificateStore(settings(link1, false))) {
        NoSuchFileException error = assertThrows(NoSuchFileException.class, store::initialize);
        assertTrue(error.getMessage().contains(link1.toString()));
        assertTrue(error.getMessage().contains(missingTarget.toString()));
        assertTrue(Files.isSymbolicLink(link1));
        assertTrue(Files.isSymbolicLink(link2));
        assertFalse(Files.exists(missingTarget, LinkOption.NOFOLLOW_LINKS));

        Files.createDirectory(missingDir);
        store.initialize();

        assertTrue(Files.isSymbolicLink(link1));
        assertTrue(Files.isSymbolicLink(link2));
        assertTrue(Files.isRegularFile(missingTarget, LinkOption.NOFOLLOW_LINKS));
        assertNotNull(loadKeyStore(missingTarget));
      }
    }

    // A symlink cycle must fail cleanly rather than looping forever or overwriting either link.
    @Test
    void initializeFailsOnSymlinkLoopAndPreservesLinks() throws Exception {
      Path root = directory.toRealPath();
      Path linkA = root.resolve("a.pfx");
      Path linkB = root.resolve("b.pfx");

      createSymbolicLink(linkA, linkB);
      createSymbolicLink(linkB, linkA);

      try (var store = new KeyStoreCertificateStore(settings(linkA, false))) {
        assertThrows(FileSystemException.class, store::initialize);

        assertTrue(Files.isSymbolicLink(linkA));
        assertTrue(Files.isSymbolicLink(linkB));
      }
    }

    // Relative '..' segments after a symlinked directory must resolve consistently with the OS
    // filesystem provider (real parent on POSIX, lexical normalization on Windows) and keep the
    // configured symlink intact.
    @Test
    void initializeResolvesSymlinkThroughSymlinkedParentDirectory() throws Exception {
      Path root = directory.toRealPath();
      boolean posix = root.getFileSystem().supportedFileAttributeViews().contains("posix");
      Path configDir = Files.createDirectory(root.resolve("config"));
      Path subDir = Files.createDirectory(root.resolve("sub"));
      Path deepDir = Files.createDirectory(subDir.resolve("deep"));
      Path posixTargetDir = Files.createDirectory(subDir.resolve("target-dir"));
      Path lexicalTargetDir = Files.createDirectory(configDir.resolve("target-dir"));

      Path dirLink = configDir.resolve("dir-link");
      createSymbolicLink(dirLink, deepDir);

      Path relativeTarget = Path.of("dir-link/../target-dir/real.pfx");
      Path link = configDir.resolve("identity.pfx");
      createSymbolicLink(link, relativeTarget);

      Path expectedParent = configDir.resolve(relativeTarget).getParent().toRealPath();
      Path expectedTarget = expectedParent.resolve("real.pfx");

      try (var store = new KeyStoreCertificateStore(settings(link, false))) {
        store.initialize();

        assertTrue(Files.isSymbolicLink(link));
        assertTrue(Files.isRegularFile(expectedTarget, LinkOption.NOFOLLOW_LINKS));
        assertEquals(expectedTarget, link.toRealPath());
        if (posix) {
          assertEquals(posixTargetDir, expectedParent);
          assertFalse(
              Files.exists(lexicalTargetDir.resolve("real.pfx"), LinkOption.NOFOLLOW_LINKS));
        }
        assertNotNull(loadKeyStore(expectedTarget));
      }
    }

    // When the configured path itself contains '..' across a symlinked parent directory,
    // configureWatchService() must resolve the parent natively so initialization writes to the
    // OS-resolved target and watches it for external changes on both POSIX and Windows.
    @Test
    void initializeWatchesConfiguredPathWithDotDotAcrossSymlinkedParentDirectory()
        throws Exception {

      Path root = directory.toRealPath();
      boolean posix = root.getFileSystem().supportedFileAttributeViews().contains("posix");
      Path configDir = Files.createDirectory(root.resolve("config"));
      Path volumeDir = Files.createDirectory(root.resolve("volume"));
      Path deepDir = Files.createDirectory(volumeDir.resolve("deep"));
      Path dirLink = configDir.resolve("dir-link");

      createSymbolicLink(dirLink, deepDir);

      Path configuredPath = dirLink.resolve("..").resolve("identity.pfx");
      Path expectedParent = configuredPath.getParent().toRealPath();
      Path expectedTarget = expectedParent.resolve("identity.pfx");

      try (var store = new KeyStoreCertificateStore(settings(configuredPath, true))) {
        store.initialize();

        assertTrue(Files.isRegularFile(expectedTarget, LinkOption.NOFOLLOW_LINKS));
        assertEquals(expectedTarget, configuredPath.toRealPath());
        if (posix) {
          assertEquals(volumeDir, expectedParent);
          assertFalse(Files.exists(configDir.resolve("identity.pfx"), LinkOption.NOFOLLOW_LINKS));
        }

        NodeId watchedId = new NodeId(2, "dotdot-watched-entry");
        try (var writer =
            KeyStoreCertificateStore.createAndInitialize(settings(expectedTarget, false))) {
          writer.set(watchedId, newEntry());
        }
        assertTrue(
            awaitContains(store, watchedId),
            "watcher must observe updates to the real target behind dir-link/..");
      }
    }

    // initialize() must pass the already-resolved dangling target into storeKeyStore() rather than
    // resolving the symlink a second time, so a concurrent retarget to an existing populated
    // KeyStore never overwrites that populated store with a freshly created empty KeyStore. When
    // watching is enabled, initialize() must also reconcile in-memory state with the new target
    // after arming the watch so callers immediately observe target B's identity.
    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void initializeDoesNotOverwriteTargetIfSymlinkRetargetsDuringCreation(boolean watchForChanges)
        throws Exception {

      Path root = directory.toRealPath();
      Path configDir = Files.createDirectory(root.resolve("config"));
      Path secretsDir = Files.createDirectory(root.resolve("secrets"));
      Path targetA = secretsDir.resolve("a.pfx");
      Path targetB = secretsDir.resolve("b.pfx");
      Path link = configDir.resolve("identity.pfx");

      NodeId existingId = NodeIds.RsaSha256ApplicationCertificateType;
      CertificateStore.Entry existingEntry = newEntry();
      try (var seed = KeyStoreCertificateStore.createAndInitialize(settings(targetB, false))) {
        seed.set(existingId, existingEntry);
      }

      createSymbolicLink(link, targetA);

      var retargeted = new AtomicBoolean(false);
      var raceSettings =
          new KeyStoreCertificateStore.Settings(
              link,
              () -> {
                if (retargeted.compareAndSet(false, true)) {
                  try {
                    retarget(link, targetB);
                  } catch (IOException e) {
                    throw new RuntimeException(e);
                  }
                }
                return "password".toCharArray();
              },
              alias -> "password".toCharArray(),
              watchForChanges);

      try (var store = new KeyStoreCertificateStore(raceSettings)) {
        store.initialize();

        if (watchForChanges) {
          assertTrue(
              store.contains(existingId),
              "watched store must immediately reconcile with retargeted KeyStore B");
          assertEquals(
              existingEntry.certificateChain[0], store.get(existingId).certificateChain[0]);
        }
      }

      assertTrue(retargeted.get());
      assertTrue(Files.isRegularFile(targetA, LinkOption.NOFOLLOW_LINKS));
      try (var checkB = KeyStoreCertificateStore.createAndInitialize(settings(targetB, false))) {
        assertTrue(
            checkB.contains(existingId),
            "populated target B must not be overwritten with an empty KeyStore");
        assertEquals(existingEntry.certificateChain[0], checkB.get(existingId).certificateChain[0]);
      }
    }

    // If a watched dangling symlink retargets during initialize() to a KeyStore that fails to load
    // (for example, protected by a different password), initialize() must propagate the failure,
    // close the armed WatchService, and remain retryable once the correct password is supplied.
    @Test
    void initializeFailsAndRemainsRetryableWhenRetargetedKeyStoreFailsToLoadDuringWatchSetup()
        throws Exception {

      Path root = directory.toRealPath();
      Path configDir = Files.createDirectory(root.resolve("config"));
      Path secretsDir = Files.createDirectory(root.resolve("secrets"));
      Path targetA = secretsDir.resolve("a.pfx");
      Path targetB = secretsDir.resolve("b.pfx");
      Path link = configDir.resolve("identity.pfx");

      NodeId existingId = NodeIds.RsaSha256ApplicationCertificateType;
      CertificateStore.Entry existingEntry = newEntry();
      var targetBSettings =
          new KeyStoreCertificateStore.Settings(
              targetB, "secret-b"::toCharArray, alias -> "secret-b".toCharArray(), false);
      try (var seed = KeyStoreCertificateStore.createAndInitialize(targetBSettings)) {
        seed.set(existingId, existingEntry);
      }

      createSymbolicLink(link, targetA);

      var retargeted = new AtomicBoolean(false);
      var currentPassword = new AtomicReference<>("password-a");
      var raceSettings =
          new KeyStoreCertificateStore.Settings(
              link,
              () -> {
                if (retargeted.compareAndSet(false, true)) {
                  try {
                    retarget(link, targetB);
                  } catch (IOException e) {
                    throw new RuntimeException(e);
                  }
                }
                return currentPassword.get().toCharArray();
              },
              alias -> currentPassword.get().toCharArray(),
              true);

      try (var store = new KeyStoreCertificateStore(raceSettings)) {
        assertThrows(IOException.class, store::initialize);
        assertThrows(
            IllegalStateException.class,
            () -> store.contains(existingId),
            "failed reconciliation during initialize() must leave the store uninitialized");

        // Supplying target B's password lets a retry of initialize() succeed and load B's identity.
        currentPassword.set("secret-b");
        store.initialize();

        assertTrue(store.contains(existingId));
        assertEquals(existingEntry.certificateChain[0], store.get(existingId).certificateChain[0]);
      }
    }

    // When a parent directory along the configured path is a symlink (current/identity.pfx where
    // current -> v1), watchForChanges must retain the original configured path for event-time
    // re-resolution so rotating current -> v2 reloads v2's identity instead of stale v1.
    @Test
    void watcherReResolvesConfiguredPathAfterParentDirectorySymlinkRotates() throws Exception {
      Path root = directory.toRealPath();
      Path v1Dir = Files.createDirectory(root.resolve("v1"));
      Path v2Dir = Files.createDirectory(root.resolve("v2"));
      Path v1Target = v1Dir.resolve("identity.pfx");
      Path v2Target = v2Dir.resolve("identity.pfx");
      Path currentLink = root.resolve("current");

      NodeId v1Id = new NodeId(2, "v1-identity");
      NodeId v2Id = new NodeId(2, "v2-identity");
      try (var w1 = KeyStoreCertificateStore.createAndInitialize(settings(v1Target, false));
          var w2 = KeyStoreCertificateStore.createAndInitialize(settings(v2Target, false))) {
        w1.set(v1Id, newEntry());
        w2.set(v2Id, newEntry());
      }

      createSymbolicLink(currentLink, v1Dir);
      Path configuredPath = currentLink.resolve("identity.pfx");

      try (var store =
          KeyStoreCertificateStore.createAndInitialize(settings(configuredPath, true))) {
        assertTrue(store.contains(v1Id));
        assertFalse(store.contains(v2Id));

        // Rotate parent symlink current -> v2 and trigger a file event on the watched v1 entry.
        retarget(currentLink, v2Dir);
        try (var trigger =
            KeyStoreCertificateStore.createAndInitialize(settings(v1Target, false))) {
          trigger.set(new NodeId(2, "v1-trigger"), newEntry());
        }

        assertTrue(
            awaitContains(store, v2Id),
            "watcher must re-resolve current/identity.pfx and load v2 identity after rotation");
        assertFalse(store.contains(v1Id), "stale v1 identity must be replaced after reload");
      }
    }

    // Resolving a path containing '..' must match the native real path for both existing and new
    // KeyStore files across platforms.
    @Test
    void initializeResolvesConfiguredPathWithDotDotToNativeRealPath() throws Exception {
      Path root = directory.toRealPath();
      Path subDir = Files.createDirectory(root.resolve("sub"));
      Path existingTarget = root.resolve("existing.pfx");
      Path existingViaDotDot = subDir.resolve("..").resolve("existing.pfx");
      Path newViaDotDot = subDir.resolve("..").resolve("created.pfx");

      NodeId seededId = NodeIds.RsaSha256ApplicationCertificateType;
      try (var seed =
          KeyStoreCertificateStore.createAndInitialize(settings(existingTarget, false))) {
        seed.set(seededId, newEntry());
      }

      assertEquals(
          existingTarget, KeyStoreCertificateStore.resolvePathFollowingLinks(existingViaDotDot));
      assertEquals(
          root.resolve("created.pfx"),
          KeyStoreCertificateStore.resolvePathFollowingLinks(newViaDotDot));

      try (var store =
          KeyStoreCertificateStore.createAndInitialize(settings(existingViaDotDot, false))) {
        assertTrue(store.contains(seededId));
      }
    }

    // On POSIX filesystems, traversing '..' through a directory lacking search (execute)
    // permission must propagate AccessDeniedException rather than lexically discarding the
    // untraversable directory and writing into its parent.
    @Test
    void initializeFailsWithAccessDeniedWhenUntraversableDirectoryPrecedesDotDot()
        throws Exception {

      Path root = directory.toRealPath();
      assumeTrue(
          root.getFileSystem().supportedFileAttributeViews().contains("posix"),
          "requires POSIX directory permissions");

      Path blockedDir = Files.createDirectory(root.resolve("blocked"));
      Path throughBlocked = blockedDir.resolve("..").resolve("identity.pfx");

      Files.setPosixFilePermissions(blockedDir, EnumSet.noneOf(PosixFilePermission.class));
      try {
        // Root (UID 0) bypasses POSIX mode 000 directory search checks; skip when running as root.
        boolean kernelDeniesAccess = false;
        try {
          Files.readAttributes(throughBlocked, BasicFileAttributes.class);
        } catch (AccessDeniedException e) {
          kernelDeniesAccess = true;
        } catch (IOException ignored) {
          // Not AccessDeniedException (e.g. root got NoSuchFileException).
        }
        assumeTrue(kernelDeniesAccess, "running user bypasses POSIX directory search permissions");

        try (var store = new KeyStoreCertificateStore(settings(throughBlocked, false))) {
          assertThrows(AccessDeniedException.class, store::initialize);
        }
        assertFalse(Files.exists(root.resolve("identity.pfx"), LinkOption.NOFOLLOW_LINKS));
      } finally {
        Files.setPosixFilePermissions(
            blockedDir,
            EnumSet.of(
                PosixFilePermission.OWNER_READ,
                PosixFilePermission.OWNER_WRITE,
                PosixFilePermission.OWNER_EXECUTE));
      }
    }
  }

  @Nested
  class RuntimeMutations {

    // If the target file behind a symlink is deleted while the store is running (parent still
    // exists), set() and remove() must recreate the file at the link target and keep the symlink.
    @Test
    void setAndRemoveRecreateDeletedTargetWhilePreservingSymlink() throws Exception {
      Path root = directory.toRealPath();
      Path configDir = Files.createDirectory(root.resolve("config"));
      Path secretsDir = Files.createDirectory(root.resolve("secrets"));
      Path target = secretsDir.resolve("application.pfx");
      Path link = configDir.resolve("identity.pfx");

      createSymbolicLink(link, configDir.relativize(target));

      NodeId initialId = NodeIds.RsaSha256ApplicationCertificateType;
      NodeId addedId = new NodeId(2, "recreated-after-delete");

      try (var store = KeyStoreCertificateStore.createAndInitialize(settings(link, false))) {
        store.set(initialId, newEntry());

        // Delete the target file so the symlink becomes dangling at runtime.
        Files.delete(target);
        assertTrue(Files.isSymbolicLink(link));
        assertFalse(Files.exists(target, LinkOption.NOFOLLOW_LINKS));

        store.set(addedId, newEntry());

        assertTrue(Files.isSymbolicLink(link), "set() must preserve dangling symlink");
        assertTrue(Files.isRegularFile(target, LinkOption.NOFOLLOW_LINKS));
        KeyStore afterSet = loadKeyStore(target);
        assertTrue(afterSet.isKeyEntry(store.getAlias(initialId)));
        assertTrue(afterSet.isKeyEntry(store.getAlias(addedId)));

        // Delete the target again and verify remove() also recreates at target and preserves link.
        Files.delete(target);
        assertNotNull(store.remove(addedId));

        assertTrue(Files.isSymbolicLink(link), "remove() must preserve dangling symlink");
        assertTrue(Files.isRegularFile(target, LinkOption.NOFOLLOW_LINKS));
        assertFalse(store.contains(addedId));
        KeyStore afterRemove = loadKeyStore(target);
        assertTrue(afterRemove.isKeyEntry(store.getAlias(initialId)));
        assertFalse(afterRemove.containsAlias(store.getAlias(addedId)));
      }
    }

    // If the volume/directory backing a symlink disappears at runtime, set() must fail naming the
    // link and target, roll back the in-memory store, and preserve the dangling symlink.
    @Test
    void setFailsAndRollsBackMemoryWhenSymlinkTargetDirectoryDisappearsAtRuntime()
        throws Exception {
      Path root = directory.toRealPath();
      Path configDir = Files.createDirectory(root.resolve("config"));
      Path secretsDir = Files.createDirectory(root.resolve("secrets"));
      Path target = secretsDir.resolve("application.pfx");
      Path link = configDir.resolve("identity.pfx");

      createSymbolicLink(link, target);

      NodeId existingId = NodeIds.RsaSha256ApplicationCertificateType;
      NodeId failedId = new NodeId(2, "unmounted-write");

      try (var store = KeyStoreCertificateStore.createAndInitialize(settings(link, false))) {
        store.set(existingId, newEntry());

        // Simulate backing volume unmount by deleting the target file and its parent directory.
        Files.delete(target);
        Files.delete(secretsDir);

        NoSuchFileException error =
            assertThrows(NoSuchFileException.class, () -> store.set(failedId, newEntry()));
        assertTrue(error.getMessage().contains(link.toString()));
        assertTrue(error.getMessage().contains(target.toString()));

        assertTrue(Files.isSymbolicLink(link), "dangling symlink must not be replaced");
        assertTrue(store.contains(existingId), "existing entry must remain intact in memory");
        assertFalse(store.contains(failedId), "failed set() must be rolled back in memory");
      }
    }

    // If the backing directory disappears before remove(), remove() must fail naming the link and
    // target, restore the entry in memory, and preserve the dangling symlink.
    @Test
    void removeFailsAndRollsBackMemoryWhenSymlinkTargetDirectoryDisappearsAtRuntime()
        throws Exception {

      Path root = directory.toRealPath();
      Path configDir = Files.createDirectory(root.resolve("config"));
      Path secretsDir = Files.createDirectory(root.resolve("secrets"));
      Path target = secretsDir.resolve("application.pfx");
      Path link = configDir.resolve("identity.pfx");

      createSymbolicLink(link, target);

      NodeId existingId = NodeIds.RsaSha256ApplicationCertificateType;

      try (var store = KeyStoreCertificateStore.createAndInitialize(settings(link, false))) {
        store.set(existingId, newEntry());

        Files.delete(target);
        Files.delete(secretsDir);

        NoSuchFileException error =
            assertThrows(NoSuchFileException.class, () -> store.remove(existingId));
        assertTrue(error.getMessage().contains(link.toString()));
        assertTrue(error.getMessage().contains(target.toString()));

        assertTrue(Files.isSymbolicLink(link), "dangling symlink must not be replaced");
        assertTrue(store.contains(existingId), "failed remove() must restore entry in memory");
        assertNotNull(store.get(existingId));
      }
    }
  }

  private static void createSymbolicLink(Path link, Path target) {
    try {
      Files.createSymbolicLink(link, target);
    } catch (IOException | UnsupportedOperationException e) {
      assumeTrue(false, "symbolic links unavailable: " + e);
    }
  }

  private static void retarget(Path link, Path target) throws IOException {
    Path replacement = link.resolveSibling(link.getFileName() + ".replacement");
    Files.deleteIfExists(replacement);
    Files.createSymbolicLink(replacement, target);
    try {
      Files.move(
          replacement, link, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      Files.delete(link);
      Files.move(replacement, link);
    }
  }

  private static KeyStoreCertificateStore.Settings settings(Path path, boolean watchForChanges) {
    return new KeyStoreCertificateStore.Settings(
        path, "password"::toCharArray, alias -> "password".toCharArray(), watchForChanges);
  }

  private static KeyStore loadKeyStore(Path path) throws Exception {
    KeyStore keyStore = KeyStore.getInstance("pkcs12");
    try (var inputStream = Files.newInputStream(path)) {
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
}
