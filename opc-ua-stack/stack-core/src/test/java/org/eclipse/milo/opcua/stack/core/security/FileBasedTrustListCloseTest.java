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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FileBasedTrustListCloseTest {

  private static final X509Certificate TRUSTED = certificate("urn:eclipse:milo:test:trusted");
  private static final X509Certificate OTHER = certificate("urn:eclipse:milo:test:other");

  @TempDir Path directory;

  // An interrupt during close() signals shutdown cancellation; close() converts it to IOException,
  // so it must restore the interrupt flag, still unload the trust list, and ensure an in-flight
  // watcher reload cannot repopulate the cleared snapshot once the lock is released.
  @Test
  void interruptedCloseRestoresInterruptStatusClearsTrustListAndBlocksInFlightReload()
      throws Exception {

    Path baseDir = Files.createTempDirectory(directory, "interrupted-close");
    Path trustedCerts = Files.createDirectories(baseDir.resolve("trusted/certs"));
    Files.write(trustedCerts.resolve("existing.der"), TRUSTED.getEncoded());

    var manager = FileBasedTrustListManager.createAndInitialize(baseDir);
    assertEquals(List.of(TRUSTED), manager.getTrustedCertificates());

    Thread watcher = manager.getWatchThread();
    assertNotNull(watcher, "initialized manager should have a watcher thread");

    var holding = new CountDownLatch(1);
    var release = new CountDownLatch(1);
    var holder =
        new Thread(
            () ->
                manager.update(
                    current -> {
                      holding.countDown();
                      awaitUninterruptibly(release);
                      return current;
                    }),
            "trust-list-lock-holder");

    // Run interrupted close() on its own thread so a regression that blocks on mutationLock fails
    // this bounded join instead of deadlocking the test thread that releases the holder.
    var closeThrown = new AtomicReference<Throwable>();
    var interruptedAfterClose = new AtomicBoolean(false);
    var trustedRightAfterClose = new AtomicReference<List<X509Certificate>>();
    var closeThread =
        new Thread(
            () -> {
              Thread.currentThread().interrupt();
              try {
                manager.close();
              } catch (Throwable t) {
                closeThrown.set(t);
              } finally {
                interruptedAfterClose.set(Thread.interrupted());
                trustedRightAfterClose.set(manager.getTrustedCertificates());
              }
            },
            "interrupted-close-caller");

    holder.start();
    try {
      assertTrue(holding.await(5, TimeUnit.SECONDS), "holder did not acquire mutation lock");

      Files.write(trustedCerts.resolve("other.der"), OTHER.getEncoded());
      awaitBlockedInSynchronize(watcher);

      closeThread.start();
      closeThread.join(5000);
      assertFalse(closeThread.isAlive(), "interrupted close() blocked while mutationLock was held");
    } finally {
      release.countDown();
      holder.join(5000);
      watcher.join(5000);
      closeThread.join(5000);
    }

    IOException thrown = assertInstanceOf(IOException.class, closeThrown.get());
    assertInstanceOf(InterruptedException.class, thrown.getCause());
    assertTrue(interruptedAfterClose.get(), "close() cleared the caller's interrupt status");
    assertEquals(List.of(), trustedRightAfterClose.get(), "close() left the trust list loaded");
    assertFalse(holder.isAlive(), "holder thread did not terminate");
    assertFalse(watcher.isAlive(), "watcher thread did not terminate");
    assertEquals(
        List.of(),
        manager.getTrustedCertificates(),
        "in-flight reload repopulated the trust list after close()");
  }

  // Even when no watcher thread is alive for join() to throw on, an interrupted close() must not
  // silently return success while another thread still holds mutationLock; lockInterruptibly()
  // must surface IOException(InterruptedException), preserve the interrupt flag, and clear state.
  @Test
  void interruptedCloseWithNoWatcherThrowsIoExceptionWhenWriterHoldsLock() throws Exception {
    Path baseDir = Files.createTempDirectory(directory, "interrupted-close-no-watcher");
    FileBasedTrustListManager manager = createUninitializedManager(baseDir);
    manager.addTrustedCertificate(TRUSTED);

    var holding = new CountDownLatch(1);
    var release = new CountDownLatch(1);
    var holder =
        new Thread(
            () ->
                manager.update(
                    current -> {
                      holding.countDown();
                      awaitUninterruptibly(release);
                      return current;
                    }),
            "no-watcher-lock-holder");

    var closeThrown = new AtomicReference<Throwable>();
    var interruptedAfterClose = new AtomicBoolean(false);
    var trustedRightAfterClose = new AtomicReference<List<X509Certificate>>();
    var closeThread =
        new Thread(
            () -> {
              Thread.currentThread().interrupt();
              try {
                manager.close();
              } catch (Throwable t) {
                closeThrown.set(t);
              } finally {
                interruptedAfterClose.set(Thread.interrupted());
                trustedRightAfterClose.set(manager.getTrustedCertificates());
              }
            },
            "interrupted-no-watcher-close-caller");

    holder.start();
    try {
      assertTrue(holding.await(5, TimeUnit.SECONDS), "holder did not acquire mutation lock");

      closeThread.start();
      closeThread.join(5000);
      assertFalse(closeThread.isAlive(), "interrupted close() blocked while mutationLock was held");
    } finally {
      release.countDown();
      holder.join(5000);
      closeThread.join(5000);
    }

    IOException thrown = assertInstanceOf(IOException.class, closeThrown.get());
    assertInstanceOf(InterruptedException.class, thrown.getCause());
    assertTrue(interruptedAfterClose.get(), "close() cleared the caller's interrupt status");
    assertEquals(List.of(), trustedRightAfterClose.get(), "close() left the trust list loaded");
    assertFalse(holder.isAlive(), "holder thread did not terminate");
  }

  // An uninterrupted close() must wait for an in-flight writer holding mutationLock to finish
  // before returning even after watchThread.join() expires while the watcher is queued behind the
  // writer.
  @Test
  void normalCloseWaitsForInFlightUpdateEvenAfterWatcherJoinExpires() throws Exception {
    Path baseDir = Files.createTempDirectory(directory, "normal-close-waits");
    Path trustedCerts = Files.createDirectories(baseDir.resolve("trusted/certs"));
    Files.write(trustedCerts.resolve("existing.der"), TRUSTED.getEncoded());

    var manager = FileBasedTrustListManager.createAndInitialize(baseDir);
    manager.watchThreadJoinTimeoutMillis = 100L;

    Thread watcher = manager.getWatchThread();
    assertNotNull(watcher, "initialized manager should have a watcher thread");

    var holding = new CountDownLatch(1);
    var release = new CountDownLatch(1);
    var holderFinished = new AtomicBoolean(false);
    var closeSawHolderFinished = new AtomicBoolean(false);
    var closeFailure = new AtomicReference<Throwable>();

    var holder =
        new Thread(
            () ->
                manager.update(
                    current -> {
                      holding.countDown();
                      awaitUninterruptibly(release);
                      holderFinished.set(true);
                      return current;
                    }),
            "normal-close-lock-holder");

    var closeThread =
        new Thread(
            () -> {
              try {
                manager.close();
                closeSawHolderFinished.set(holderFinished.get());
              } catch (Throwable t) {
                closeFailure.set(t);
              }
            },
            "normal-close-caller");

    holder.start();
    try {
      assertTrue(holding.await(5, TimeUnit.SECONDS), "holder did not acquire mutation lock");

      // Queue a watcher reload behind holder so join(100) expires while the watcher is still alive.
      Files.write(trustedCerts.resolve("other.der"), OTHER.getEncoded());
      awaitBlockedInSynchronize(watcher);

      closeThread.start();
      // Thread.join(timeout) is TIMED_WAITING; reaching WAITING inside close() proves join()
      // expired and close() entered mutationLock.lockInterruptibly() instead of returning early.
      awaitBlockedInClose(closeThread);
      assertTrue(watcher.isAlive(), "watcher should still be queued behind holder");
      assertTrue(closeThread.isAlive(), "normal close() returned before in-flight update finished");

      release.countDown();
      holder.join(5000);
      watcher.join(5000);
      closeThread.join(5000);
    } finally {
      release.countDown();
      holder.join(5000);
      watcher.join(5000);
      closeThread.join(5000);
    }

    assertEquals(null, closeFailure.get());
    assertFalse(holder.isAlive(), "holder thread did not terminate");
    assertFalse(watcher.isAlive(), "watcher thread did not terminate");
    assertFalse(closeThread.isAlive(), "close thread did not terminate");
    assertTrue(
        closeSawHolderFinished.get(), "normal close() returned before in-flight update completed");
    assertEquals(List.of(), manager.getTrustedCertificates());
  }

  // A reload that has already entered synchronize() before close() begins must not overwrite the
  // cleared snapshot when its directory scan finishes.
  @Test
  void inFlightReloadCannotRepopulateSnapshotAfterClose() throws Exception {
    Path baseDir = Files.createTempDirectory(directory, "in-flight-reload");
    FileBasedTrustListManager manager = createUninitializedManager(baseDir);
    manager.addTrustedCertificate(TRUSTED);

    var enteredReload = new CountDownLatch(1);
    var finishReload = new CountDownLatch(1);
    var reloadFailure = new AtomicReference<Throwable>();

    var reloadThread =
        new Thread(
            () -> {
              try {
                manager.synchronize(
                    "trusted certificates",
                    current -> {
                      enteredReload.countDown();
                      awaitUninterruptibly(finishReload);
                      return current.withTrustedCertificates(List.of(OTHER));
                    });
              } catch (Throwable t) {
                reloadFailure.set(t);
              }
            },
            "in-flight-reload-test");

    var closeFailure = new AtomicReference<Throwable>();
    var closeThread =
        new Thread(
            () -> {
              try {
                manager.close();
              } catch (Throwable t) {
                closeFailure.set(t);
              }
            },
            "concurrent-close-test");

    reloadThread.start();
    try {
      assertTrue(enteredReload.await(5, TimeUnit.SECONDS), "reload did not start");

      closeThread.start();
      awaitEmptyTrustedCertificates(manager);

      finishReload.countDown();
      reloadThread.join(5000);
      closeThread.join(5000);
    } finally {
      finishReload.countDown();
      reloadThread.join(5000);
      closeThread.join(5000);
    }

    assertFalse(reloadThread.isAlive(), "reload thread did not finish");
    assertFalse(closeThread.isAlive(), "close thread did not finish");
    assertEquals(null, reloadFailure.get());
    assertEquals(null, closeFailure.get());
    assertEquals(
        List.of(),
        manager.getTrustedCertificates(),
        "in-flight reload must not repopulate snapshot after close()");
  }

  // A reload waiting on mutationLock when close() runs must see the closed flag under the lock
  // and return without executing its reload callback or republishing.
  @Test
  void queuedReloadSkipsReloadCallbackAfterClose() throws Exception {
    Path baseDir = Files.createTempDirectory(directory, "queued-reload");
    FileBasedTrustListManager manager = createUninitializedManager(baseDir);
    manager.addTrustedCertificate(TRUSTED);

    var holding = new CountDownLatch(1);
    var release = new CountDownLatch(1);
    var reloadBodyInvoked = new AtomicBoolean(false);
    var holderFailure = new AtomicReference<Throwable>();
    var reloadFailure = new AtomicReference<Throwable>();
    var closeFailure = new AtomicReference<Throwable>();

    var holder =
        new Thread(
            () -> {
              try {
                manager.update(
                    current -> {
                      holding.countDown();
                      awaitUninterruptibly(release);
                      return current;
                    });
              } catch (Throwable t) {
                holderFailure.set(t);
              }
            },
            "queued-reload-holder");

    var reloadThread =
        new Thread(
            () -> {
              try {
                manager.synchronize(
                    "trusted certificates",
                    current -> {
                      reloadBodyInvoked.set(true);
                      return current.withTrustedCertificates(List.of(OTHER));
                    });
              } catch (Throwable t) {
                reloadFailure.set(t);
              }
            },
            "queued-reload-worker");

    var closeThread =
        new Thread(
            () -> {
              try {
                manager.close();
              } catch (Throwable t) {
                closeFailure.set(t);
              }
            },
            "queued-reload-close");

    holder.start();
    try {
      assertTrue(holding.await(5, TimeUnit.SECONDS), "holder did not acquire mutation lock");

      reloadThread.start();
      awaitBlockedInSynchronize(reloadThread);

      closeThread.start();
      awaitEmptyTrustedCertificates(manager);

      release.countDown();
      holder.join(5000);
      reloadThread.join(5000);
      closeThread.join(5000);
    } finally {
      release.countDown();
      holder.join(5000);
      reloadThread.join(5000);
      closeThread.join(5000);
    }

    assertFalse(holder.isAlive(), "holder thread did not finish");
    assertFalse(reloadThread.isAlive(), "reload thread did not finish");
    assertFalse(closeThread.isAlive(), "close thread did not finish");
    assertEquals(null, holderFailure.get());
    assertEquals(null, reloadFailure.get());
    assertEquals(null, closeFailure.get());
    assertFalse(reloadBodyInvoked.get(), "closed manager should skip queued directory reload");
    assertEquals(List.of(), manager.getTrustedCertificates());
  }

  // Control: an uninterrupted close() stops the watcher, clears all lists, preserves
  // lastUpdateTime, leaves the interrupt status clear, and is safe to call more than once.
  @Test
  void normalCloseClearsTrustListPreservesLastUpdateTimeAndIsIdempotent() throws Exception {
    Path baseDir = Files.createTempDirectory(directory, "normal-close");
    Path trustedCerts = Files.createDirectories(baseDir.resolve("trusted/certs"));
    Files.write(trustedCerts.resolve("existing.der"), TRUSTED.getEncoded());

    var manager = FileBasedTrustListManager.createAndInitialize(baseDir);
    assertEquals(List.of(TRUSTED), manager.getTrustedCertificates());

    DateTime lastUpdateTime = manager.getLastUpdateTime();
    Thread watcher = manager.getWatchThread();
    assertNotNull(watcher);

    manager.close();

    assertFalse(Thread.currentThread().isInterrupted());
    assertFalse(watcher.isAlive(), "watcher thread should stop on close()");
    assertEquals(List.of(), manager.getTrustedCertificates());
    assertEquals(List.of(), manager.getIssuerCertificates());
    assertEquals(List.of(), manager.getTrustedCrls());
    assertEquals(List.of(), manager.getIssuerCrls());
    assertEquals(lastUpdateTime, manager.getLastUpdateTime());

    manager.close();
    assertEquals(List.of(), manager.getTrustedCertificates());
    assertEquals(lastUpdateTime, manager.getLastUpdateTime());
  }

  // A closed manager must reject further updates and re-initialization instead of writing files
  // or repopulating its cleared snapshot.
  @Test
  void updateAndInitializeAfterCloseThrowIllegalStateException() throws Exception {
    Path baseDir = Files.createTempDirectory(directory, "post-close");
    var manager = FileBasedTrustListManager.createAndInitialize(baseDir);
    manager.close();

    assertThrows(IllegalStateException.class, () -> manager.addTrustedCertificate(TRUSTED));
    assertThrows(IllegalStateException.class, manager::initialize);
    assertEquals(List.of(), manager.getTrustedCertificates());

    try (Stream<Path> files = Files.list(baseDir.resolve("trusted/certs"))) {
      assertEquals(0, files.count(), "update after close() must not write files");
    }
  }

  private static FileBasedTrustListManager createUninitializedManager(Path baseDir)
      throws IOException {

    Path issuerCertsDir = Files.createDirectories(baseDir.resolve("issuer/certs"));
    Path issuerCrlDir = Files.createDirectories(baseDir.resolve("issuer/crl"));
    Path trustedCertsDir = Files.createDirectories(baseDir.resolve("trusted/certs"));
    Path trustedCrlDir = Files.createDirectories(baseDir.resolve("trusted/crl"));

    return new FileBasedTrustListManager(
        issuerCertsDir, issuerCrlDir, trustedCertsDir, trustedCrlDir);
  }

  private static void awaitBlockedInSynchronize(Thread thread) {
    awaitBlockedInMethod(thread, "synchronize");
  }

  private static void awaitBlockedInClose(Thread thread) {
    awaitBlockedInMethod(thread, "close");
  }

  private static void awaitBlockedInMethod(Thread thread, String methodName) {
    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(15);

    while (System.nanoTime() < deadline) {
      if (thread.getState() == Thread.State.WAITING
          && Arrays.stream(thread.getStackTrace())
              .anyMatch(
                  frame ->
                      frame.getClassName().equals(FileBasedTrustListManager.class.getName())
                          && frame.getMethodName().equals(methodName))) {
        return;
      }
      LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(5));
    }

    throw new AssertionError(
        "thread "
            + thread.getName()
            + " did not block in "
            + methodName
            + "(); state="
            + thread.getState());
  }

  private static void awaitEmptyTrustedCertificates(FileBasedTrustListManager manager) {
    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(5);

    while (System.nanoTime() < deadline) {
      if (manager.getTrustedCertificates().isEmpty()) {
        return;
      }
      Thread.onSpinWait();
    }

    throw new AssertionError("close() did not clear trusted certificates within 5 seconds");
  }

  private static void awaitUninterruptibly(CountDownLatch latch) {
    while (true) {
      try {
        latch.await();
        return;
      } catch (InterruptedException ignored) {
        // Keep holding the lock until released by the test.
      }
    }
  }

  private static X509Certificate certificate(String applicationUri) {
    try {
      KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);

      return SelfSignedCertificateBuilder.forRsaApplicationCertificate(keyPair)
          .setCommonName(applicationUri)
          .setApplicationUri(applicationUri)
          .build();
    } catch (Exception e) {
      throw new ExceptionInInitializerError(e);
    }
  }
}
