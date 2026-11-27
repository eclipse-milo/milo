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
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.cert.CRLException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
import org.eclipse.milo.opcua.stack.core.util.WatchKeyRunner;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link TrustListManager} backed by directories containing certificates and CRLs.
 *
 * <p>{@link #initialize()} starts a background thread that watches the configured directories for
 * changes. Each OPC UA application should create one shared instance and call {@link #close()} when
 * the application shuts down.
 *
 * <p>Every change is published as one immutable {@link TrustListSnapshot}. API updates write
 * affected certificate and CRL files by syncing a hidden temporary file and renaming it into place
 * with {@code ATOMIC_MOVE} when supported by the filesystem, falling back to {@code
 * REPLACE_EXISTING} otherwise, and publish all four lists together. If any write or delete fails,
 * affected directories are re-read under the manager's lock to reconcile memory with disk before
 * throwing {@link UncheckedIOException}; if re-reading an affected directory also fails, that
 * directory retains its previous in-memory entries and the read error is attached as a suppressed
 * exception. A directory reload triggered by the watcher replaces only the list for that directory.
 *
 * <p>A reload decodes every regular file in a directory, whatever its extension, and logs and skips
 * files that fail to decode. Hidden files, whose names start with {@code .}, and subdirectories are
 * not read, so files such as the {@code .DS_Store} that macOS Finder creates neither appear in the
 * trust list nor produce warnings.
 */
public class FileBasedTrustListManager implements TrustListManager, Closeable {

  private static final Logger LOGGER = LoggerFactory.getLogger(FileBasedTrustListManager.class);

  private final Lock mutationLock = new ReentrantLock();

  private final AtomicReference<State> state =
      new AtomicReference<>(new State(TrustListSnapshot.empty(), false));

  private volatile @Nullable Thread watchThread;
  private volatile @Nullable WatchService watchService;

  /** Timeout in milliseconds to wait for the watcher thread during {@link #close()}. */
  long watchThreadJoinTimeoutMillis = 5000L;

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

    Preconditions.checkArgument(Files.exists(issuerCertsDir), "issuerCertsDir does not exist");
    Preconditions.checkArgument(Files.exists(issuerCrlDir), "issuerCrlDir does not exist");
    Preconditions.checkArgument(Files.exists(trustedCertsDir), "trustedCertsDir does not exist");
    Preconditions.checkArgument(Files.exists(trustedCrlDir), "trustedCrlDir does not exist");
  }

  /**
   * Load the configured directories and start watching them for changes.
   *
   * @throws IOException if the directories cannot be watched.
   * @throws IllegalStateException if this manager has been closed.
   */
  public void initialize() throws IOException {
    if (state.get().closed()) {
      throw new IllegalStateException("FileBasedTrustListManager is closed");
    }

    WatchService service = issuerCertsDir.getFileSystem().newWatchService();

    try {
      Map<WatchKey, Runnable> watchKeys = new ConcurrentHashMap<>();
      watchKeys.put(register(service, issuerCertsDir), this::synchronizeIssuerCertificates);
      watchKeys.put(register(service, issuerCrlDir), this::synchronizeIssuerCrls);
      watchKeys.put(register(service, trustedCertsDir), this::synchronizeTrustedCertificates);
      watchKeys.put(register(service, trustedCrlDir), this::synchronizeTrustedCrls);

      watchService = service;

      synchronizeIssuerCertificates();
      synchronizeIssuerCrls();
      synchronizeTrustedCertificates();
      synchronizeTrustedCrls();

      if (state.get().closed()) {
        throw new IllegalStateException("FileBasedTrustListManager is closed");
      }

      Thread thread = new Thread(new WatchKeyRunner(service, watchKeys));
      thread.setName("milo-trust-list-watcher");
      thread.setDaemon(true);
      watchThread = thread;
      thread.start();
    } catch (IOException | RuntimeException e) {
      try {
        service.close();
      } catch (IOException closeFailure) {
        e.addSuppressed(closeFailure);
      }

      throw e;
    }
  }

  private static WatchKey register(WatchService watchService, Path directory) throws IOException {
    return directory.register(
        watchService,
        StandardWatchEventKinds.ENTRY_CREATE,
        StandardWatchEventKinds.ENTRY_DELETE,
        StandardWatchEventKinds.ENTRY_MODIFY);
  }

  /**
   * Stop watching the configured directories and clear the in-memory trust lists.
   *
   * <p>If this thread is interrupted while waiting for shutdown to finish, the interrupt status is
   * restored, the trust lists are still cleared, and an {@link IOException} is thrown. Subsequent
   * updates fail with {@link IllegalStateException}.
   *
   * @throws IOException if closing the watch service fails or waiting for shutdown is interrupted.
   */
  @Override
  public void close() throws IOException {
    clearAndMarkClosed();

    IOException failure = null;

    try {
      WatchService service = watchService;
      if (service != null) {
        try {
          service.close();
        } catch (IOException e) {
          failure = e;
          Thread thread = watchThread;
          if (thread != null) {
            thread.interrupt();
          }
        }
      }

      Thread thread = watchThread;
      if (thread != null) {
        try {
          thread.join(watchThreadJoinTimeoutMillis);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          failure = combineFailures(failure, new IOException(e));
        }
      }
    } finally {
      if (failure == null) {
        try {
          mutationLock.lockInterruptibly();
          try {
            clearAndMarkClosed();
          } finally {
            mutationLock.unlock();
          }
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          clearAndMarkClosed();
          failure = new IOException(e);
        }
      } else if (mutationLock.tryLock()) {
        try {
          clearAndMarkClosed();
        } finally {
          mutationLock.unlock();
        }
      } else {
        clearAndMarkClosed();
      }
    }

    if (failure != null) {
      throw failure;
    }
  }

  @Override
  public TrustListSnapshot getSnapshot() {
    return state.get().snapshot();
  }

  /**
   * {@inheritDoc}
   *
   * <p>Files for added and removed entries are persisted before the snapshot is published. If any
   * write or delete fails, the affected directories are re-read under the manager's lock to
   * reconcile memory with disk before throwing {@link UncheckedIOException}; if re-reading an
   * affected directory also fails, that directory retains its previous in-memory entries and the
   * read error is attached as a suppressed exception.
   *
   * @throws IllegalStateException if this manager has been closed.
   * @throws UncheckedIOException if persisting or removing any affected file fails.
   */
  @Override
  public TrustListSnapshot update(UnaryOperator<TrustListSnapshot> update) {
    mutationLock.lock();
    try {
      State currentState = state.get();
      if (currentState.closed()) {
        throw new IllegalStateException("FileBasedTrustListManager is closed");
      }

      TrustListSnapshot current = currentState.snapshot();
      TrustListSnapshot updated = Objects.requireNonNull(update.apply(current));

      if (updated == current) {
        return state.get().snapshot();
      }

      if (state.get().closed()) {
        throw new IllegalStateException("FileBasedTrustListManager is closed");
      }

      boolean issuerCertsDirty = false;
      boolean issuerCrlsDirty = false;
      boolean trustedCertsDirty = false;
      boolean trustedCrlsDirty = false;

      try {
        if (!current.issuerCertificates().equals(updated.issuerCertificates())) {
          issuerCertsDirty = true;
          updateFiles(
              current.issuerCertificates(),
              updated.issuerCertificates(),
              issuerCertsDir,
              this::writeCertificateToDir,
              this::deleteCertificatesFromDir);
        }
        if (!current.issuerCrls().equals(updated.issuerCrls())) {
          issuerCrlsDirty = true;
          updateFiles(
              current.issuerCrls(),
              updated.issuerCrls(),
              issuerCrlDir,
              this::writeCrlToDir,
              this::deleteCrlsFromDir);
        }
        if (!current.trustedCertificates().equals(updated.trustedCertificates())) {
          trustedCertsDirty = true;
          updateFiles(
              current.trustedCertificates(),
              updated.trustedCertificates(),
              trustedCertsDir,
              this::writeCertificateToDir,
              this::deleteCertificatesFromDir);
        }
        if (!current.trustedCrls().equals(updated.trustedCrls())) {
          trustedCrlsDirty = true;
          updateFiles(
              current.trustedCrls(),
              updated.trustedCrls(),
              trustedCrlDir,
              this::writeCrlToDir,
              this::deleteCrlsFromDir);
        }
      } catch (IOException e) {
        reconcileAfterFailure(
            current, issuerCertsDirty, issuerCrlsDirty, trustedCertsDirty, trustedCrlsDirty, e);

        LOGGER.error("Failed to persist trust list update", e);
        throw new UncheckedIOException(e);
      }

      TrustListSnapshot committed = updated.withLastUpdateTime(DateTime.now());

      return publishSnapshot(committed);
    } finally {
      mutationLock.unlock();
    }
  }

  @Override
  public List<X509CRL> getIssuerCrls() {
    return state.get().snapshot().issuerCrls();
  }

  @Override
  public List<X509CRL> getTrustedCrls() {
    return state.get().snapshot().trustedCrls();
  }

  @Override
  public List<X509Certificate> getIssuerCertificates() {
    return state.get().snapshot().issuerCertificates();
  }

  @Override
  public List<X509Certificate> getTrustedCertificates() {
    return state.get().snapshot().trustedCertificates();
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
    return state.get().snapshot().lastUpdateTime();
  }

  @Nullable Thread getWatchThread() {
    return watchThread;
  }

  private void clearAndMarkClosed() {
    state.updateAndGet(
        current ->
            new State(
                TrustListSnapshot.empty().withLastUpdateTime(current.snapshot().lastUpdateTime()),
                true));
  }

  private TrustListSnapshot publishSnapshot(TrustListSnapshot next) {
    return state
        .updateAndGet(current -> current.closed() ? current : new State(next, false))
        .snapshot();
  }

  private static IOException combineFailures(@Nullable IOException primary, IOException next) {
    if (primary == null) {
      return next;
    }
    primary.addSuppressed(next);
    return primary;
  }

  private void reconcileAfterFailure(
      TrustListSnapshot previous,
      boolean issuerCertsDirty,
      boolean issuerCrlsDirty,
      boolean trustedCertsDirty,
      boolean trustedCrlsDirty,
      IOException failure) {

    TrustListSnapshot reconciled = previous;

    if (issuerCertsDirty) {
      try {
        reconciled = reconciled.withIssuerCertificates(readCertificates(issuerCertsDir));
      } catch (IOException e) {
        failure.addSuppressed(e);
      }
    }
    if (issuerCrlsDirty) {
      try {
        reconciled = reconciled.withIssuerCrls(readCrls(issuerCrlDir));
      } catch (IOException e) {
        failure.addSuppressed(e);
      }
    }
    if (trustedCertsDirty) {
      try {
        reconciled = reconciled.withTrustedCertificates(readCertificates(trustedCertsDir));
      } catch (IOException e) {
        failure.addSuppressed(e);
      }
    }
    if (trustedCrlsDirty) {
      try {
        reconciled = reconciled.withTrustedCrls(readCrls(trustedCrlDir));
      } catch (IOException e) {
        failure.addSuppressed(e);
      }
    }

    if (!hasSameEntries(previous, reconciled)) {
      publishSnapshot(reconciled.withLastUpdateTime(DateTime.now()));
    }
  }

  private static boolean hasSameEntries(TrustListSnapshot first, TrustListSnapshot second) {
    return sameEntries(first.issuerCertificates(), second.issuerCertificates())
        && sameEntries(first.issuerCrls(), second.issuerCrls())
        && sameEntries(first.trustedCertificates(), second.trustedCertificates())
        && sameEntries(first.trustedCrls(), second.trustedCrls());
  }

  private static <T> boolean sameEntries(List<T> first, List<T> second) {
    return first.size() == second.size()
        && (first.equals(second) || Set.copyOf(first).equals(Set.copyOf(second)));
  }

  private record State(TrustListSnapshot snapshot, boolean closed) {}

  @FunctionalInterface
  private interface EntryWriter<T> {
    void write(T entry, Path directory) throws IOException;
  }

  @FunctionalInterface
  private interface EntryDeleter<T> {
    void delete(Set<T> entries, Path directory) throws IOException;
  }

  /** Write files for entries added to {@code replacement} and delete files for entries removed. */
  private static <T> void updateFiles(
      List<T> current,
      List<T> replacement,
      Path directory,
      EntryWriter<T> write,
      EntryDeleter<T> delete)
      throws IOException {

    Set<T> currentSet = Set.copyOf(current);
    Set<T> replacementSet = new LinkedHashSet<>(replacement);

    for (T added : Sets.difference(replacementSet, currentSet)) {
      write.write(added, directory);
    }

    Set<T> removed = Sets.difference(currentSet, replacementSet);
    if (!removed.isEmpty()) {
      delete.delete(removed, directory);
    }
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
   * the directory or any of its files cannot be read, the previous snapshot is kept. If this
   * manager has been closed, the reload is skipped and cannot repopulate the cleared snapshot.
   */
  void synchronize(String name, Reload reload) {
    LOGGER.debug("Synchronizing {}...", name);

    mutationLock.lock();
    try {
      State currentState = state.get();
      if (currentState.closed()) {
        return;
      }

      TrustListSnapshot current = currentState.snapshot();
      TrustListSnapshot reloaded = reload.apply(current);
      if (hasSameEntries(current, reloaded)
          && !current.lastUpdateTime().equals(DateTime.MIN_VALUE)) {
        return;
      }

      publishSnapshot(reloaded.withLastUpdateTime(DateTime.now()));
    } catch (IOException e) {
      LOGGER.warn("Error synchronizing {}", name, e);
    } finally {
      mutationLock.unlock();
    }
  }

  @FunctionalInterface
  interface Reload {
    TrustListSnapshot apply(TrustListSnapshot current) throws IOException;
  }

  List<X509Certificate> readCertificates(Path directory) throws IOException {
    var certificates = new ArrayList<X509Certificate>();
    for (Path entry : listEntries(directory)) {
      if (isRegularFile(entry)) {
        decodeCertificateFile(entry).ifPresent(certificates::add);
      }
    }
    return List.copyOf(certificates);
  }

  List<X509CRL> readCrls(Path directory) throws IOException {
    var crls = new ArrayList<X509CRL>();
    for (Path entry : listEntries(directory)) {
      if (isRegularFile(entry)) {
        decodeCrlFile(entry).ifPresent(crls::addAll);
      }
    }
    return List.copyOf(crls);
  }

  /** List the entries in {@code directory} whose names do not start with {@code .}. */
  private static List<Path> listEntries(Path directory) throws IOException {
    try (Stream<Path> entries = Files.list(directory)) {
      return entries.filter(path -> !path.getFileName().toString().startsWith(".")).toList();
    } catch (UncheckedIOException e) {
      throw e.getCause();
    }
  }

  BasicFileAttributes readFileAttributes(Path path) throws IOException {
    return Files.readAttributes(path, BasicFileAttributes.class);
  }

  private boolean isRegularFile(Path path) throws IOException {
    try {
      return readFileAttributes(path).isRegularFile();
    } catch (NoSuchFileException ignored) {
      return false;
    }
  }

  InputStream openFileInputStream(Path path) throws IOException {
    return Files.newInputStream(path);
  }

  private Optional<X509Certificate> decodeCertificateFile(Path path) throws IOException {
    InputStream raw;
    try {
      raw = openFileInputStream(path);
    } catch (NoSuchFileException ignored) {
      return Optional.empty();
    }

    try (var in = new TrackingInputStream(raw)) {
      try {
        X509Certificate certificate = CertificateUtil.decodeCertificate(in);
        IOException failure = in.readFailure();
        if (failure != null) {
          throw failure;
        }
        return Optional.of(certificate);
      } catch (UaException | RuntimeException e) {
        IOException failure = in.readFailure();
        if (failure != null) {
          throw failure;
        }

        // This repeats on every reload of the directory, so the stack trace is logged at DEBUG.
        LOGGER.warn("Error decoding certificate file {}: {}", path, e.toString());
        LOGGER.debug("Error decoding certificate file {}", path, e);

        return Optional.empty();
      }
    }
  }

  private Optional<List<X509CRL>> decodeCrlFile(Path path) throws IOException {
    InputStream raw;
    try {
      raw = openFileInputStream(path);
    } catch (NoSuchFileException ignored) {
      return Optional.empty();
    }

    try (var in = new TrackingInputStream(raw)) {
      try {
        List<X509CRL> crls = CertificateUtil.decodeCrls(in);
        IOException failure = in.readFailure();
        if (failure != null) {
          throw failure;
        }
        return Optional.of(crls);
      } catch (UaException | RuntimeException e) {
        IOException failure = in.readFailure();
        if (failure != null) {
          throw failure;
        }

        // This repeats on every reload of the directory, so the stack trace is logged at DEBUG.
        LOGGER.warn("Error decoding CRL file {}: {}", path, e.toString());
        LOGGER.debug("Error decoding CRL file {}", path, e);

        return Optional.empty();
      }
    }
  }

  private static final class TrackingInputStream extends FilterInputStream {

    private @Nullable IOException readFailure;

    TrackingInputStream(InputStream in) {
      super(in);
    }

    @Nullable IOException readFailure() {
      return readFailure;
    }

    private IOException record(IOException e) {
      if (readFailure == null) {
        readFailure = e;
      }
      return e;
    }

    @Override
    public int read() throws IOException {
      try {
        return in.read();
      } catch (IOException e) {
        throw record(e);
      }
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
      try {
        return in.read(b, off, len);
      } catch (IOException e) {
        throw record(e);
      }
    }

    @Override
    public long skip(long n) throws IOException {
      try {
        return in.skip(n);
      } catch (IOException e) {
        throw record(e);
      }
    }

    @Override
    public int available() throws IOException {
      try {
        return in.available();
      } catch (IOException e) {
        throw record(e);
      }
    }

    @Override
    public synchronized void reset() throws IOException {
      try {
        in.reset();
      } catch (IOException e) {
        throw record(e);
      }
    }
  }

  private void writeCertificateToDir(X509Certificate certificate, Path directory)
      throws IOException {

    byte[] encoded;
    try {
      encoded = certificate.getEncoded();
    } catch (CertificateEncodingException e) {
      throw new IOException("Failed to encode certificate", e);
    }

    String thumbprint = ByteBufUtil.hexDump(sha1(encoded));
    Path target = directory.resolve(thumbprint + ".der");

    writeFileAtomically(target, ".cert-", encoded);

    LOGGER.debug("Wrote certificate: {}", target);
  }

  private void deleteCertificatesFromDir(Set<X509Certificate> certificates, Path directory)
      throws IOException {

    IOException failure = null;

    for (Path entry : listEntries(directory)) {
      try {
        if (isRegularFile(entry)
            && decodeCertificateFile(entry).filter(certificates::contains).isPresent()) {
          deleteFile(entry);
          LOGGER.debug("Deleted certificate: {}", entry);
        }
      } catch (IOException e) {
        LOGGER.error("Error deleting certificate: {}", entry, e);
        failure = combineFailures(failure, e);
      }
    }

    if (failure != null) {
      throw failure;
    }
  }

  private void writeCrlToDir(X509CRL crl, Path directory) throws IOException {
    byte[] encoded;
    try {
      encoded = crl.getEncoded();
    } catch (CRLException e) {
      throw new IOException("Failed to encode CRL", e);
    }

    String thumbprint = ByteBufUtil.hexDump(sha1(encoded));
    Path target = directory.resolve(thumbprint + ".crl");

    writeFileAtomically(target, ".crl-", encoded);

    LOGGER.debug("Wrote CRL: {}", target);
  }

  private void deleteCrlsFromDir(Set<X509CRL> removed, Path directory) throws IOException {
    IOException failure = null;

    for (Path entry : listEntries(directory)) {
      try {
        if (!isRegularFile(entry)) {
          continue;
        }

        List<X509CRL> contents = decodeCrlFile(entry).orElse(List.of());
        List<X509CRL> retained = contents.stream().filter(crl -> !removed.contains(crl)).toList();
        if (retained.size() == contents.size()) {
          continue;
        }

        if (retained.isEmpty()) {
          deleteFile(entry);
          LOGGER.debug("Deleted CRL file: {}", entry);
        } else {
          replaceCrlFile(entry, retained);
          LOGGER.debug("Removed CRLs from bundle: {}", entry);
        }
      } catch (IOException e) {
        LOGGER.error("Error removing CRLs from file: {}", entry, e);
        failure = combineFailures(failure, e);
      }
    }

    if (failure != null) {
      throw failure;
    }
  }

  private void replaceCrlFile(Path file, List<X509CRL> crls) throws IOException {
    // Encode the surviving members completely before writing, so an encoding failure cannot disturb
    // the unrelated CRLs already stored in the bundle.
    var output = new ByteArrayOutputStream();
    for (X509CRL crl : crls) {
      try {
        output.write(crl.getEncoded());
      } catch (CRLException e) {
        throw new IOException("Failed to encode CRL", e);
      }
    }

    writeFileAtomically(file, ".crl-", output.toByteArray());
  }

  void writeFileAtomically(Path target, String tempPrefix, byte[] bytes) throws IOException {
    Path parent = Objects.requireNonNull(target.getParent(), "target has no parent directory");
    Path temporary = createTemporaryFile(parent, tempPrefix, bytes);
    Throwable failure = null;

    try {
      if (target.getFileSystem().supportedFileAttributeViews().contains("posix")
          && isRegularFile(target)) {
        try {
          Files.setPosixFilePermissions(temporary, Files.getPosixFilePermissions(target));
        } catch (NoSuchFileException ignored) {
          // Target disappeared between the check and attribute read; keep umask permissions.
        }
      }

      try {
        Files.move(
            temporary, target, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
      } catch (AtomicMoveNotSupportedException e) {
        Files.move(temporary, target, StandardCopyOption.REPLACE_EXISTING);
      }
    } catch (Throwable t) {
      failure = t;
      throw t;
    } finally {
      try {
        Files.deleteIfExists(temporary);
      } catch (IOException cleanupFailure) {
        if (failure != null) {
          failure.addSuppressed(cleanupFailure);
        } else {
          throw cleanupFailure;
        }
      }
    }
  }

  private static Path createTemporaryFile(Path parent, String tempPrefix, byte[] bytes)
      throws IOException {

    while (true) {
      Path candidate = parent.resolve(tempPrefix + UUID.randomUUID() + ".tmp");
      boolean created = false;
      try (FileChannel channel =
          FileChannel.open(candidate, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)) {
        created = true;
        // Create with CREATE_NEW and no explicit POSIX attribute so new files inherit the process
        // umask (unlike Files.createTempFile, which forces 0600).
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        while (buffer.hasRemaining()) {
          channel.write(buffer);
        }
        // Force the contents to stable storage before the rename publishes the file, so a crash
        // cannot leave a truncated certificate or CRL in place of the original.
        channel.force(true);
        return candidate;
      } catch (FileAlreadyExistsException ignored) {
        // Retry with a fresh random suffix.
      } catch (Throwable t) {
        if (created) {
          try {
            Files.deleteIfExists(candidate);
          } catch (IOException cleanupFailure) {
            t.addSuppressed(cleanupFailure);
          }
        }
        throw t;
      }
    }
  }

  void deleteFile(Path file) throws IOException {
    Files.deleteIfExists(file);
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
