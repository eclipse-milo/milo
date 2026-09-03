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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.UnaryOperator;
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
 * the application shuts down. Directory reloads and API updates publish all four lists as one
 * immutable snapshot.
 */
public class FileBasedTrustListManager implements TrustListManager, Closeable {

  private static final Logger LOGGER = LoggerFactory.getLogger(FileBasedTrustListManager.class);

  private final Lock mutationLock = new ReentrantLock();

  private final AtomicReference<TrustListSnapshot> snapshot =
      new AtomicReference<>(
          new TrustListSnapshot(List.of(), List.of(), List.of(), List.of(), DateTime.MIN_VALUE));

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

    watchKeys.put(
        issuerCertsDir.register(
            watchService,
            StandardWatchEventKinds.ENTRY_CREATE,
            StandardWatchEventKinds.ENTRY_DELETE,
            StandardWatchEventKinds.ENTRY_MODIFY),
        this::synchronizeAll);
    watchKeys.put(
        issuerCrlDir.register(
            watchService,
            StandardWatchEventKinds.ENTRY_CREATE,
            StandardWatchEventKinds.ENTRY_DELETE,
            StandardWatchEventKinds.ENTRY_MODIFY),
        this::synchronizeAll);
    watchKeys.put(
        trustedCertsDir.register(
            watchService,
            StandardWatchEventKinds.ENTRY_CREATE,
            StandardWatchEventKinds.ENTRY_DELETE,
            StandardWatchEventKinds.ENTRY_MODIFY),
        this::synchronizeAll);
    watchKeys.put(
        trustedCrlDir.register(
            watchService,
            StandardWatchEventKinds.ENTRY_CREATE,
            StandardWatchEventKinds.ENTRY_DELETE,
            StandardWatchEventKinds.ENTRY_MODIFY),
        this::synchronizeAll);

    synchronizeAll();

    watchThread = new Thread(new WatchKeyRunner(watchService, watchKeys));
    watchThread.setName("milo-trust-list-watcher");
    watchThread.setDaemon(true);
    watchThread.start();
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
      TrustListSnapshot current = snapshot.get();
      snapshot.set(
          new TrustListSnapshot(
              List.of(), List.of(), List.of(), List.of(), current.lastUpdateTime()));
    } finally {
      mutationLock.unlock();
    }
  }

  @Override
  public TrustListSnapshot getSnapshot() {
    return snapshot.get();
  }

  @Override
  public void replaceAll(TrustListSnapshot snapshot) {
    mutationLock.lock();
    try {
      replaceAllLocked(snapshot);
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
    updateSnapshot(
        current ->
            new TrustListSnapshot(
                current.issuerCertificates(),
                issuerCrls,
                current.trustedCertificates(),
                current.trustedCrls(),
                DateTime.now()));
  }

  @Override
  public void setTrustedCrls(List<X509CRL> trustedCrls) {
    updateSnapshot(
        current ->
            new TrustListSnapshot(
                current.issuerCertificates(),
                current.issuerCrls(),
                current.trustedCertificates(),
                trustedCrls,
                DateTime.now()));
  }

  @Override
  public void setIssuerCertificates(List<X509Certificate> issuerCertificates) {
    updateSnapshot(
        current ->
            new TrustListSnapshot(
                issuerCertificates,
                current.issuerCrls(),
                current.trustedCertificates(),
                current.trustedCrls(),
                DateTime.now()));
  }

  @Override
  public void setTrustedCertificates(List<X509Certificate> trustedCertificates) {
    updateSnapshot(
        current ->
            new TrustListSnapshot(
                current.issuerCertificates(),
                current.issuerCrls(),
                trustedCertificates,
                current.trustedCrls(),
                DateTime.now()));
  }

  @Override
  public void addIssuerCertificate(X509Certificate certificate) {
    updateSnapshot(
        current -> {
          var issuerCertificates = new HashSet<>(current.issuerCertificates());
          issuerCertificates.add(certificate);

          return new TrustListSnapshot(
              List.copyOf(issuerCertificates),
              current.issuerCrls(),
              current.trustedCertificates(),
              current.trustedCrls(),
              DateTime.now());
        });
  }

  @Override
  public void addTrustedCertificate(X509Certificate certificate) {
    updateSnapshot(
        current -> {
          var trustedCertificates = new HashSet<>(current.trustedCertificates());
          trustedCertificates.add(certificate);

          return new TrustListSnapshot(
              current.issuerCertificates(),
              current.issuerCrls(),
              List.copyOf(trustedCertificates),
              current.trustedCrls(),
              DateTime.now());
        });
  }

  @Override
  public boolean removeIssuerCertificate(ByteString thumbprint) {
    mutationLock.lock();
    try {
      deleteCertificateFromDir(thumbprint, issuerCertsDir);

      TrustListSnapshot current = snapshot.get();
      var issuerCertificates = new HashSet<>(current.issuerCertificates());
      boolean removed = remove(thumbprint, issuerCertificates);

      if (removed) {
        snapshot.set(
            new TrustListSnapshot(
                List.copyOf(issuerCertificates),
                current.issuerCrls(),
                current.trustedCertificates(),
                current.trustedCrls(),
                DateTime.now()));
      }

      return removed;
    } finally {
      mutationLock.unlock();
    }
  }

  @Override
  public boolean removeTrustedCertificate(ByteString thumbprint) {
    mutationLock.lock();
    try {
      deleteCertificateFromDir(thumbprint, trustedCertsDir);

      TrustListSnapshot current = snapshot.get();
      var trustedCertificates = new HashSet<>(current.trustedCertificates());
      boolean removed = remove(thumbprint, trustedCertificates);

      if (removed) {
        snapshot.set(
            new TrustListSnapshot(
                current.issuerCertificates(),
                current.issuerCrls(),
                List.copyOf(trustedCertificates),
                current.trustedCrls(),
                DateTime.now()));
      }

      return removed;
    } finally {
      mutationLock.unlock();
    }
  }

  private static boolean remove(ByteString thumbprint, Set<X509Certificate> certificates) {
    return certificates.removeIf(
        certificate -> {
          try {
            return CertificateUtil.thumbprint(certificate).equals(thumbprint);
          } catch (UaException ignored) {
            return false;
          }
        });
  }

  @Override
  public DateTime getLastUpdateTime() {
    return snapshot.get().lastUpdateTime();
  }

  private void updateSnapshot(UnaryOperator<TrustListSnapshot> update) {
    mutationLock.lock();
    try {
      replaceAllLocked(update.apply(snapshot.get()));
    } finally {
      mutationLock.unlock();
    }
  }

  private void replaceAllLocked(TrustListSnapshot replacement) {
    TrustListSnapshot current = snapshot.get();
    TrustListSnapshot normalized = normalize(replacement);

    updateCertificateFiles(
        current.issuerCertificates(), normalized.issuerCertificates(), issuerCertsDir);
    updateCrlFiles(current.issuerCrls(), normalized.issuerCrls(), issuerCrlDir);
    updateCertificateFiles(
        current.trustedCertificates(), normalized.trustedCertificates(), trustedCertsDir);
    updateCrlFiles(current.trustedCrls(), normalized.trustedCrls(), trustedCrlDir);

    snapshot.set(normalized);
  }

  private static TrustListSnapshot normalize(TrustListSnapshot snapshot) {
    return new TrustListSnapshot(
        List.copyOf(new HashSet<>(snapshot.issuerCertificates())),
        List.copyOf(new HashSet<>(snapshot.issuerCrls())),
        List.copyOf(new HashSet<>(snapshot.trustedCertificates())),
        List.copyOf(new HashSet<>(snapshot.trustedCrls())),
        snapshot.lastUpdateTime());
  }

  private static void updateCertificateFiles(
      List<X509Certificate> current, List<X509Certificate> replacement, Path directory) {

    Set<X509Certificate> currentSet = Set.copyOf(current);
    Set<X509Certificate> replacementSet = Set.copyOf(replacement);

    Sets.difference(replacementSet, currentSet)
        .forEach(certificate -> writeCertificateToDir(certificate, directory));
    Sets.difference(currentSet, replacementSet)
        .forEach(certificate -> deleteCertificateFromDir(certificate, directory));
  }

  private static void updateCrlFiles(
      List<X509CRL> current, List<X509CRL> replacement, Path directory) {

    Set<X509CRL> currentSet = Set.copyOf(current);
    Set<X509CRL> replacementSet = Set.copyOf(replacement);

    Sets.difference(replacementSet, currentSet).forEach(crl -> writeCrlToDir(crl, directory));
    Sets.difference(currentSet, replacementSet).forEach(crl -> deleteCrlFromDir(crl, directory));
  }

  private void synchronizeAll() {
    LOGGER.debug("Synchronizing trust lists...");

    mutationLock.lock();
    try {
      List<X509Certificate> issuerCertificates = readCertificates(issuerCertsDir);
      List<X509CRL> issuerCrls = readCrls(issuerCrlDir);
      List<X509Certificate> trustedCertificates = readCertificates(trustedCertsDir);
      List<X509CRL> trustedCrls = readCrls(trustedCrlDir);

      snapshot.set(
          new TrustListSnapshot(
              issuerCertificates, issuerCrls, trustedCertificates, trustedCrls, DateTime.now()));
    } catch (IOException e) {
      LOGGER.warn("Error synchronizing trust lists", e);
    } finally {
      mutationLock.unlock();
    }
  }

  private static List<X509Certificate> readCertificates(Path directory) throws IOException {
    var certificates = new HashSet<X509Certificate>();

    try (var files = Files.list(directory)) {
      files.flatMap(path -> decodeCertificateFile(path).stream()).forEach(certificates::add);
    }

    return List.copyOf(certificates);
  }

  private static List<X509CRL> readCrls(Path directory) throws IOException {
    var crls = new HashSet<X509CRL>();

    try (var files = Files.list(directory)) {
      files.flatMap(path -> decodeCrlFile(path).stream()).forEach(crls::addAll);
    }

    return List.copyOf(crls);
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
      deleteCertificateFromDir(ByteString.of(sha1(certificate.getEncoded())), path);
    } catch (Exception e) {
      LOGGER.error("Error deleting certificate", e);
    }
  }

  private static void deleteCertificateFromDir(ByteString thumbprint, Path path) {
    try {
      String filename = String.format("%s.der", ByteBufUtil.hexDump(thumbprint.bytesOrEmpty()));
      File file = path.resolve(filename).toFile();

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
      deleteCrlFromDir(ByteString.of(sha1(crl.getEncoded())), path);
    } catch (Exception e) {
      LOGGER.error("Error deleting CRL", e);
    }
  }

  private static void deleteCrlFromDir(ByteString thumbprint, Path path) {
    try {
      String filename = String.format("%s.crl", ByteBufUtil.hexDump(thumbprint.bytesOrEmpty()));
      File file = path.resolve(filename).toFile();

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
