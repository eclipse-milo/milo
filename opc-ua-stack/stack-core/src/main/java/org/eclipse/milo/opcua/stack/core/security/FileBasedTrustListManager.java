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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.security.cert.CRL;
import java.security.cert.CRLException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
import org.eclipse.milo.opcua.stack.core.util.WatchKeyRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileBasedTrustListManager implements TrustListManager, Closeable {

  private static final Logger LOGGER = LoggerFactory.getLogger(FileBasedTrustListManager.class);

  private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

  private final AtomicReference<DateTime> lastUpdateTime =
      new AtomicReference<>(DateTime.MIN_VALUE);

  private final Set<X509Certificate> issuerCertificates = new HashSet<>();
  private final Set<X509CRL> issuerCrls = new HashSet<>();

  private final Set<X509Certificate> trustedCertificates = new HashSet<>();
  private final Set<X509CRL> trustedCrls = new HashSet<>();

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

  public void initialize() throws IOException {
    watchService = FileSystems.getDefault().newWatchService();

    Map<WatchKey, Runnable> watchKeys = new ConcurrentHashMap<>();

    watchKeys.put(
        issuerCertsDir.register(
            watchService,
            StandardWatchEventKinds.ENTRY_CREATE,
            StandardWatchEventKinds.ENTRY_DELETE,
            StandardWatchEventKinds.ENTRY_MODIFY),
        this::synchronizeIssuerCerts);
    watchKeys.put(
        issuerCrlDir.register(
            watchService,
            StandardWatchEventKinds.ENTRY_CREATE,
            StandardWatchEventKinds.ENTRY_DELETE,
            StandardWatchEventKinds.ENTRY_MODIFY),
        this::synchronizeIssuerCrl);
    watchKeys.put(
        trustedCertsDir.register(
            watchService,
            StandardWatchEventKinds.ENTRY_CREATE,
            StandardWatchEventKinds.ENTRY_DELETE,
            StandardWatchEventKinds.ENTRY_MODIFY),
        this::synchronizeTrustedCerts);
    watchKeys.put(
        trustedCrlDir.register(
            watchService,
            StandardWatchEventKinds.ENTRY_CREATE,
            StandardWatchEventKinds.ENTRY_DELETE,
            StandardWatchEventKinds.ENTRY_MODIFY),
        this::synchronizeTrustedCrl);

    watchThread = new Thread(new WatchKeyRunner(watchService, watchKeys));
    watchThread.setName("milo-trust-list-watcher");
    watchThread.setDaemon(true);
    watchThread.start();

    synchronizeIssuerCerts();
    synchronizeIssuerCrl();
    synchronizeTrustedCerts();
    synchronizeTrustedCrl();

    lastUpdateTime.set(DateTime.now());
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

    readWriteLock.writeLock().lock();
    try {
      issuerCertificates.clear();
      issuerCrls.clear();
      trustedCertificates.clear();
      trustedCrls.clear();
    } finally {
      readWriteLock.writeLock().unlock();
    }
  }

  @Override
  public List<X509CRL> getIssuerCrls() {
    readWriteLock.readLock().lock();
    try {
      return List.copyOf(issuerCrls);
    } finally {
      readWriteLock.readLock().unlock();
    }
  }

  @Override
  public List<X509CRL> getTrustedCrls() {
    readWriteLock.readLock().lock();
    try {
      return List.copyOf(trustedCrls);
    } finally {
      readWriteLock.readLock().unlock();
    }
  }

  @Override
  public List<X509Certificate> getIssuerCertificates() {
    readWriteLock.readLock().lock();
    try {
      return List.copyOf(issuerCertificates);
    } finally {
      readWriteLock.readLock().unlock();
    }
  }

  @Override
  public List<X509Certificate> getTrustedCertificates() {
    readWriteLock.readLock().lock();
    try {
      return List.copyOf(trustedCertificates);
    } finally {
      readWriteLock.readLock().unlock();
    }
  }

  @Override
  public void setIssuerCrls(List<X509CRL> issuerCrls) {
    readWriteLock.writeLock().lock();
    try {
      Set<X509CRL> oldSet = Set.copyOf(this.issuerCrls);

      this.issuerCrls.clear();
      this.issuerCrls.addAll(issuerCrls);

      Set<X509CRL> toWrite = Sets.difference(this.issuerCrls, oldSet);
      Set<X509CRL> toDelete = Sets.difference(oldSet, this.issuerCrls);

      toWrite.forEach(crl -> writeCrlToDir(crl, issuerCrlDir));
      toDelete.forEach(crl -> deleteCrlFromDir(crl, issuerCrlDir));
    } finally {
      readWriteLock.writeLock().unlock();
    }
  }

  @Override
  public void setTrustedCrls(List<X509CRL> trustedCrls) {
    readWriteLock.writeLock().lock();
    try {
      Set<X509CRL> oldSet = Set.copyOf(this.trustedCrls);

      this.trustedCrls.clear();
      this.trustedCrls.addAll(trustedCrls);

      Set<X509CRL> toWrite = Sets.difference(this.trustedCrls, oldSet);
      Set<X509CRL> toDelete = Sets.difference(oldSet, this.trustedCrls);

      toWrite.forEach(crl -> writeCrlToDir(crl, trustedCrlDir));
      toDelete.forEach(crl -> deleteCrlFromDir(crl, trustedCrlDir));
    } finally {
      readWriteLock.writeLock().unlock();
    }
  }

  @Override
  public void setIssuerCertificates(List<X509Certificate> issuerCertificates) {
    readWriteLock.writeLock().lock();
    try {
      Set<X509Certificate> oldSet = Set.copyOf(this.issuerCertificates);

      this.issuerCertificates.clear();
      this.issuerCertificates.addAll(issuerCertificates);

      Set<X509Certificate> toWrite = Sets.difference(this.issuerCertificates, oldSet);
      Set<X509Certificate> toDelete = Sets.difference(oldSet, this.issuerCertificates);

      toWrite.forEach(cert -> writeCertificateToDir(cert, issuerCertsDir));
      toDelete.forEach(cert -> deleteCertificateFromDir(cert, issuerCertsDir));
    } finally {
      readWriteLock.writeLock().unlock();
    }
  }

  @Override
  public void setTrustedCertificates(List<X509Certificate> trustedCertificates) {
    readWriteLock.writeLock().lock();
    try {
      Set<X509Certificate> oldSet = Set.copyOf(this.trustedCertificates);

      this.trustedCertificates.clear();
      this.trustedCertificates.addAll(trustedCertificates);

      Set<X509Certificate> toWrite = Sets.difference(this.trustedCertificates, oldSet);
      Set<X509Certificate> toDelete = Sets.difference(oldSet, this.trustedCertificates);

      toWrite.forEach(cert -> writeCertificateToDir(cert, trustedCertsDir));
      toDelete.forEach(cert -> deleteCertificateFromDir(cert, trustedCertsDir));
    } finally {
      readWriteLock.writeLock().unlock();
    }
  }

  @Override
  public void addIssuerCertificate(X509Certificate certificate) {
    readWriteLock.writeLock().lock();
    try {
      issuerCertificates.add(certificate);

      writeCertificateToDir(certificate, issuerCertsDir);
    } finally {
      readWriteLock.writeLock().unlock();
    }
  }

  @Override
  public void addTrustedCertificate(X509Certificate certificate) {
    readWriteLock.writeLock().lock();
    try {
      trustedCertificates.add(certificate);

      writeCertificateToDir(certificate, trustedCertsDir);
    } finally {
      readWriteLock.writeLock().unlock();
    }
  }

  @Override
  public boolean removeIssuerCertificate(ByteString thumbprint) {
    readWriteLock.writeLock().lock();
    try {
      deleteCertificateFromDir(thumbprint, issuerCertsDir);

      return remove(thumbprint, issuerCertificates);
    } finally {
      readWriteLock.writeLock().unlock();
    }
  }

  @Override
  public boolean removeTrustedCertificate(ByteString thumbprint) {
    readWriteLock.writeLock().lock();
    try {
      deleteCertificateFromDir(thumbprint, trustedCertsDir);

      return remove(thumbprint, trustedCertificates);
    } finally {
      readWriteLock.writeLock().unlock();
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
    return lastUpdateTime.get();
  }

  private void synchronizeIssuerCerts() {
    LOGGER.debug("Synchronizing issuer certs...");

    readWriteLock.writeLock().lock();
    try {
      issuerCertificates.clear();

      try (var files = Files.list(issuerCertsDir)) {
        files.flatMap(c -> decodeCertificateFile(c).stream()).forEach(issuerCertificates::add);
      }
    } catch (IOException e) {
      LOGGER.warn("Error synchronizing issuer certs", e);
    } finally {
      readWriteLock.writeLock().unlock();
    }
  }

  private void synchronizeIssuerCrl() {
    LOGGER.debug("Synchronizing issuer CRLs...");

    readWriteLock.writeLock().lock();
    try {
      issuerCrls.clear();

      try (var files = Files.list(issuerCrlDir)) {
        files.flatMap(c -> decodeCrlFile(c).stream()).forEach(issuerCrls::addAll);
      }
    } catch (IOException e) {
      LOGGER.warn("Error synchronizing issuer CRLs", e);
    } finally {
      readWriteLock.writeLock().unlock();
    }
  }

  private void synchronizeTrustedCerts() {
    LOGGER.debug("Synchronizing trusted certs...");

    readWriteLock.writeLock().lock();
    try {
      trustedCertificates.clear();

      try (var files = Files.list(trustedCertsDir)) {
        files.flatMap(c -> decodeCertificateFile(c).stream()).forEach(trustedCertificates::add);
      }
    } catch (IOException e) {
      LOGGER.warn("Error synchronizing trusted certs", e);
    } finally {
      readWriteLock.writeLock().unlock();
    }
  }

  private void synchronizeTrustedCrl() {
    LOGGER.debug("Synchronizing trusted CRLs...");

    readWriteLock.writeLock().lock();
    try {
      trustedCrls.clear();

      try (var files = Files.list(trustedCrlDir)) {
        files.flatMap(c -> decodeCrlFile(c).stream()).forEach(trustedCrls::addAll);
      }
    } catch (IOException e) {
      LOGGER.warn("Error synchronizing trusted CRLs", e);
    } finally {
      readWriteLock.writeLock().unlock();
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
    try {
      CertificateFactory factory = CertificateFactory.getInstance("X.509");
      Collection<? extends CRL> crls = factory.generateCRLs(new FileInputStream(path.toFile()));

      return Optional.of(
          crls.stream()
              .filter(crl -> crl instanceof X509CRL)
              .map(X509CRL.class::cast)
              .collect(Collectors.toList()));
    } catch (CertificateException | FileNotFoundException | CRLException e) {
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
