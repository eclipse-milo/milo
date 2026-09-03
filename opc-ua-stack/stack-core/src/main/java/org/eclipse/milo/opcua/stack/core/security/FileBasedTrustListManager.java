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

import static org.eclipse.milo.opcua.stack.core.util.DigestUtil.sha1;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import io.netty.buffer.ByteBufUtil;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
import org.eclipse.milo.opcua.stack.core.util.WatchKeyRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link TrustListManager} backed by directories containing certificates and CRLs.
 *
 * <p>{@link #initialize()} starts a background thread that watches the configured directories for
 * changes. Each OPC UA application should create one shared instance and call {@link #close()} when
 * the application shuts down.
 *
 * <p>Every change is published as one immutable {@link TrustListSnapshot}. API updates write the
 * affected files and publish all four lists together; a directory reload triggered by the watcher
 * replaces only the list for that directory.
 */
public class FileBasedTrustListManager implements TrustListManager, Closeable {

  private static final Logger LOGGER = LoggerFactory.getLogger(FileBasedTrustListManager.class);

  private final Lock mutationLock = new ReentrantLock();

  private final AtomicReference<TrustListSnapshot> snapshot =
      new AtomicReference<>(TrustListSnapshot.empty());

  private Thread watchThread;
  private WatchService watchService;

  private final Path issuerCertsDir;
  private final Path issuerCrlDir;

  private final Path trustedCertsDir;
  private final Path trustedCrlDir;

  public FileBasedTrustListManager(
      Path issuerCertsDir, Path issuerCrlDir, Path trustedCertsDir, Path trustedCrlDir) {

    this.issuerCertsDir = issuerCertsDir;
    this.issuerCrlDir = issuerCrlDir;
    this.trustedCertsDir = trustedCertsDir;
    this.trustedCrlDir = trustedCrlDir;

    Preconditions.checkArgument(issuerCertsDir.toFile().exists(), "issuerCertsDir does not exist");
    Preconditions.checkArgument(issuerCrlDir.toFile().exists(), "issuerCrlDir does not exist");
    Preconditions.checkArgument(
        trustedCertsDir.toFile().exists(), "trustedCertsDir does not exist");
    Preconditions.checkArgument(trustedCrlDir.toFile().exists(), "trustedCrlDir does not exist");
  }

  /**
   * Load the configured directories and start watching them for changes.
   *
   * @throws IOException if the directories cannot be watched.
   */
  public void initialize() throws IOException {
    watchService = FileSystems.getDefault().newWatchService();

    Map<WatchKey, Runnable> watchKeys = new ConcurrentHashMap<>();
    watchKeys.put(register(issuerCertsDir), this::synchronizeIssuerCertificates);
    watchKeys.put(register(issuerCrlDir), this::synchronizeIssuerCrls);
    watchKeys.put(register(trustedCertsDir), this::synchronizeTrustedCertificates);
    watchKeys.put(register(trustedCrlDir), this::synchronizeTrustedCrls);

    synchronizeIssuerCertificates();
    synchronizeIssuerCrls();
    synchronizeTrustedCertificates();
    synchronizeTrustedCrls();

    watchThread = new Thread(new WatchKeyRunner(watchService, watchKeys));
    watchThread.setName("milo-trust-list-watcher");
    watchThread.setDaemon(true);
    watchThread.start();
  }

  private WatchKey register(Path directory) throws IOException {
    return directory.register(
        watchService,
        StandardWatchEventKinds.ENTRY_CREATE,
        StandardWatchEventKinds.ENTRY_DELETE,
        StandardWatchEventKinds.ENTRY_MODIFY);
  }

  @Override
  public void close() throws IOException {
    if (watchService != null) {
      watchService.close();
    }

    if (watchThread != null) {
      try {
        watchThread.join(5000);
      } catch (InterruptedException e) {
        throw new IOException(e);
      }
    }

    mutationLock.lock();
    try {
      snapshot.set(TrustListSnapshot.empty().withLastUpdateTime(snapshot.get().lastUpdateTime()));
    } finally {
      mutationLock.unlock();
    }
  }

  @Override
  public TrustListSnapshot getSnapshot() {
    return snapshot.get();
  }

  /**
   * {@inheritDoc}
   *
   * <p>Files for added and removed entries are written before the snapshot is published. A write or
   * delete that fails is logged; the published snapshot still reflects the requested state, and a
   * later directory reload reconciles it with the directory contents.
   */
  @Override
  public TrustListSnapshot update(UnaryOperator<TrustListSnapshot> update) {
    mutationLock.lock();
    try {
      TrustListSnapshot current = snapshot.get();
      TrustListSnapshot updated = Objects.requireNonNull(update.apply(current));

      if (updated == current) {
        return current;
      }

      updateFiles(
          current.issuerCertificates(),
          updated.issuerCertificates(),
          issuerCertsDir,
          FileBasedTrustListManager::writeCertificateToDir,
          FileBasedTrustListManager::deleteCertificateFromDir);
      updateFiles(
          current.issuerCrls(),
          updated.issuerCrls(),
          issuerCrlDir,
          FileBasedTrustListManager::writeCrlToDir,
          FileBasedTrustListManager::deleteCrlFromDir);
      updateFiles(
          current.trustedCertificates(),
          updated.trustedCertificates(),
          trustedCertsDir,
          FileBasedTrustListManager::writeCertificateToDir,
          FileBasedTrustListManager::deleteCertificateFromDir);
      updateFiles(
          current.trustedCrls(),
          updated.trustedCrls(),
          trustedCrlDir,
          FileBasedTrustListManager::writeCrlToDir,
          FileBasedTrustListManager::deleteCrlFromDir);

      TrustListSnapshot committed = updated.withLastUpdateTime(DateTime.now());
      snapshot.set(committed);

      return committed;
    } finally {
      mutationLock.unlock();
    }
  }

  @Override
  public List<X509CRL> getIssuerCrls() {
    return snapshot.get().issuerCrls();
  }

  @Override
  public List<X509CRL> getTrustedCrls() {
    return snapshot.get().trustedCrls();
  }

  @Override
  public List<X509Certificate> getIssuerCertificates() {
    return snapshot.get().issuerCertificates();
  }

  @Override
  public List<X509Certificate> getTrustedCertificates() {
    return snapshot.get().trustedCertificates();
  }

  @Override
  public void setIssuerCrls(List<X509CRL> issuerCrls) {
    update(current -> current.withIssuerCrls(issuerCrls));
  }

  @Override
  public void setTrustedCrls(List<X509CRL> trustedCrls) {
    update(current -> current.withTrustedCrls(trustedCrls));
  }

  @Override
  public void setIssuerCertificates(List<X509Certificate> issuerCertificates) {
    update(current -> current.withIssuerCertificates(issuerCertificates));
  }

  @Override
  public void setTrustedCertificates(List<X509Certificate> trustedCertificates) {
    update(current -> current.withTrustedCertificates(trustedCertificates));
  }

  @Override
  public void addIssuerCertificate(X509Certificate certificate) {
    update(
        current ->
            current.withIssuerCertificates(
                TrustListEdits.append(current.issuerCertificates(), certificate)));
  }

  @Override
  public void addTrustedCertificate(X509Certificate certificate) {
    update(
        current ->
            current.withTrustedCertificates(
                TrustListEdits.append(current.trustedCertificates(), certificate)));
  }

  @Override
  public boolean removeIssuerCertificate(ByteString thumbprint) {
    return TrustListEdits.remove(
        this,
        thumbprint,
        TrustListSnapshot::issuerCertificates,
        TrustListSnapshot::withIssuerCertificates);
  }

  @Override
  public boolean removeTrustedCertificate(ByteString thumbprint) {
    return TrustListEdits.remove(
        this,
        thumbprint,
        TrustListSnapshot::trustedCertificates,
        TrustListSnapshot::withTrustedCertificates);
  }

  @Override
  public DateTime getLastUpdateTime() {
    return snapshot.get().lastUpdateTime();
  }

  /** Write files for entries added to {@code replacement} and delete files for entries removed. */
  private static <T> void updateFiles(
      List<T> current,
      List<T> replacement,
      Path directory,
      BiConsumer<T, Path> write,
      BiConsumer<T, Path> delete) {

    if (current.equals(replacement)) {
      return;
    }

    Set<T> currentSet = Set.copyOf(current);
    Set<T> replacementSet = Set.copyOf(replacement);

    Sets.difference(replacementSet, currentSet).forEach(entry -> write.accept(entry, directory));
    Sets.difference(currentSet, replacementSet).forEach(entry -> delete.accept(entry, directory));
  }

  private void synchronizeIssuerCertificates() {
    synchronize(
        "issuer certificates",
        current -> current.withIssuerCertificates(readCertificates(issuerCertsDir)));
  }

  private void synchronizeIssuerCrls() {
    synchronize("issuer CRLs", current -> current.withIssuerCrls(readCrls(issuerCrlDir)));
  }

  private void synchronizeTrustedCertificates() {
    synchronize(
        "trusted certificates",
        current -> current.withTrustedCertificates(readCertificates(trustedCertsDir)));
  }

  private void synchronizeTrustedCrls() {
    synchronize("trusted CRLs", current -> current.withTrustedCrls(readCrls(trustedCrlDir)));
  }

  /**
   * Reload one directory and publish the current snapshot with that directory's list replaced. If
   * the directory cannot be listed the previous snapshot is kept.
   */
  private void synchronize(String name, Reload reload) {
    LOGGER.debug("Synchronizing {}...", name);

    mutationLock.lock();
    try {
      snapshot.set(reload.apply(snapshot.get()).withLastUpdateTime(DateTime.now()));
    } catch (IOException e) {
      LOGGER.warn("Error synchronizing {}", name, e);
    } finally {
      mutationLock.unlock();
    }
  }

  @FunctionalInterface
  private interface Reload {
    TrustListSnapshot apply(TrustListSnapshot current) throws IOException;
  }

  private static List<X509Certificate> readCertificates(Path directory) throws IOException {
    return readAll(directory, path -> decodeCertificateFile(path).stream());
  }

  private static List<X509CRL> readCrls(Path directory) throws IOException {
    return readAll(directory, path -> decodeCrlFile(path).stream().flatMap(List::stream));
  }

  private static <T> List<T> readAll(Path directory, Function<Path, Stream<T>> decode)
      throws IOException {

    try (var files = Files.list(directory)) {
      return files.flatMap(decode).toList();
    }
  }

  private static Optional<X509Certificate> decodeCertificateFile(Path path) {
    try {
      try (FileInputStream inputStream = new FileInputStream(path.toFile())) {
        return Optional.of(CertificateUtil.decodeCertificate(inputStream));
      }
    } catch (Throwable t) {
      LOGGER.warn("Error decoding certificate: {}", path, t);

      return Optional.empty();
    }
  }

  private static Optional<List<X509CRL>> decodeCrlFile(Path path) {
    try (FileInputStream inputStream = new FileInputStream(path.toFile())) {
      return Optional.of(CertificateUtil.decodeCrls(inputStream));
    } catch (UaException | IOException e) {
      LOGGER.warn("Error decoding CRL file: {}", path, e);

      return Optional.empty();
    }
  }

  private static void writeCertificateToDir(X509Certificate certificate, Path path) {
    try {
      String thumbprint = ByteBufUtil.hexDump(sha1(certificate.getEncoded()));
      String filename = String.format("%s.der", thumbprint);
      File file = path.resolve(filename).toFile();

      try (FileOutputStream fos = new FileOutputStream(file)) {
        fos.write(certificate.getEncoded());
        fos.flush();
      }

      LOGGER.debug("Wrote certificate: {}", file.getAbsolutePath());
    } catch (Exception e) {
      LOGGER.error("Error writing certificate", e);
    }
  }

  private static void deleteCertificateFromDir(X509Certificate certificate, Path path) {
    try {
      String thumbprint = ByteBufUtil.hexDump(sha1(certificate.getEncoded()));
      File file = path.resolve(String.format("%s.der", thumbprint)).toFile();

      if (file.exists()) {
        Files.delete(file.toPath());

        LOGGER.debug("Deleted certificate: {}", file.getAbsolutePath());
      }
    } catch (Exception e) {
      LOGGER.error("Error deleting certificate", e);
    }
  }

  private static void writeCrlToDir(X509CRL crl, Path path) {
    try {
      String thumbprint = ByteBufUtil.hexDump(sha1(crl.getEncoded()));
      String filename = String.format("%s.crl", thumbprint);
      File file = path.resolve(filename).toFile();

      try (FileOutputStream fos = new FileOutputStream(file)) {
        fos.write(crl.getEncoded());
        fos.flush();
      }

      LOGGER.debug("Wrote CRL: {}", file.getAbsolutePath());
    } catch (Exception e) {
      LOGGER.error("Error writing CRL", e);
    }
  }

  private static void deleteCrlFromDir(X509CRL crl, Path path) {
    try {
      String thumbprint = ByteBufUtil.hexDump(sha1(crl.getEncoded()));
      File file = path.resolve(String.format("%s.crl", thumbprint)).toFile();

      if (file.exists()) {
        Files.delete(file.toPath());

        LOGGER.debug("Deleted CRL: {}", file.getAbsolutePath());
      }
    } catch (Exception e) {
      LOGGER.error("Error deleting CRL", e);
    }
  }

  /**
   * Create and initialize a {@link FileBasedTrustListManager} at the specified {@code baseDir},
   * creating directories as necessary.
   *
   * <p>The returned manager owns a background watcher thread. Each OPC UA application should create
   * one shared instance and {@link #close() close it} when the application shuts down.
   *
   * @param baseDir the base directory to manage the Trust List in.
   * @return a new, initialized {@link FileBasedTrustListManager} instance.
   * @throws IOException if an error occurs creating directories or initializing.
   */
  public static FileBasedTrustListManager createAndInitialize(Path baseDir) throws IOException {
    Path issuerDir = baseDir.resolve("issuer");
    ensureDirectoryExists(issuerDir);

    Path issuerCertsDir = issuerDir.resolve("certs");
    ensureDirectoryExists(issuerCertsDir);

    Path issuerCrlDir = issuerDir.resolve("crl");
    ensureDirectoryExists(issuerCrlDir);

    Path trustedDir = baseDir.resolve("trusted");
    ensureDirectoryExists(trustedDir);

    Path trustedCertsDir = trustedDir.resolve("certs");
    ensureDirectoryExists(trustedCertsDir);

    Path trustedCrlDir = trustedDir.resolve("crl");
    ensureDirectoryExists(trustedCrlDir);

    var trustListManager =
        new FileBasedTrustListManager(issuerCertsDir, issuerCrlDir, trustedCertsDir, trustedCrlDir);

    trustListManager.initialize();

    return trustListManager;
  }

  private static void ensureDirectoryExists(Path dir) throws IOException {
    if (!Files.exists(dir)) {
      Files.createDirectories(dir);
    }
  }
}
