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

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.function.Supplier;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A PKCS#12-backed {@link CertificateStore}.
 *
 * <p>After {@link #initialize()} returns, operations on one instance are serialized. Before each
 * {@link #set(NodeId, Entry)} or {@link #remove(NodeId)}, the store re-reads the on-disk KeyStore,
 * so entries written by another owner before that read begins are preserved. Writes by other store
 * instances or processes are not coordinated; overlapping read-modify-replace operations are
 * last-replacement-wins.
 */
public class KeyStoreCertificateStore implements CertificateStore, Closeable {

  private static final String DEFAULT_ALIAS_PREFIX = "server-";

  /**
   * Alias suffixes for the standard application certificate types, in the order they are preloaded.
   * Custom certificate types use {@link NodeId#toParseableString()} as their alias.
   */
  private static final Map<NodeId, String> ALIAS_SUFFIXES;

  static {
    var suffixes = new LinkedHashMap<NodeId, String>();
    suffixes.put(NodeIds.RsaSha256ApplicationCertificateType, "rsa-sha256");
    suffixes.put(NodeIds.EccNistP256ApplicationCertificateType, "ecc-nistp256");
    suffixes.put(NodeIds.EccNistP384ApplicationCertificateType, "ecc-nistp384");
    suffixes.put(NodeIds.EccBrainpoolP256r1ApplicationCertificateType, "ecc-brainpoolp256r1");
    suffixes.put(NodeIds.EccBrainpoolP384r1ApplicationCertificateType, "ecc-brainpoolp384r1");
    suffixes.put(NodeIds.EccCurve25519ApplicationCertificateType, "ecc-curve25519");
    suffixes.put(NodeIds.EccCurve448ApplicationCertificateType, "ecc-curve448");
    ALIAS_SUFFIXES = Collections.unmodifiableMap(suffixes);
  }

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final AtomicBoolean initialized = new AtomicBoolean(false);

  private final ReentrantLock keyStoreLock = new ReentrantLock(true);

  private final Map<String, Entry> entries = new HashMap<>();

  private KeyStore keyStore;
  private Thread watchThread;
  private WatchService watchService;

  private final Settings settings;

  public KeyStoreCertificateStore(Settings settings) {
    this.settings = settings;
  }

  public void initialize() throws Exception {
    if (initialized.compareAndSet(false, true)) {
      logger.info("Loading KeyStore at {}", settings.keyStorePath);

      File keyStoreFile = settings.keyStorePath.toAbsolutePath().toFile();

      if (keyStoreFile.exists()) {
        keyStore = loadKeyStore(keyStoreFile.toPath());

        try {
          keyStoreLock.lock();

          loadEntries();
        } finally {
          keyStoreLock.unlock();
        }
      } else {
        keyStore = KeyStore.getInstance("pkcs12");
        keyStore.load(null, settings.getKeyStorePassword.get());

        storeKeyStore();
      }

      if (settings.watchForChanges) {
        configureWatchService(keyStoreFile);
      }
    }
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
        Thread.currentThread().interrupt();

        throw new IOException(e);
      }
    }
  }

  @Override
  public boolean contains(NodeId certificateTypeId) throws Exception {
    if (!initialized.get()) {
      throw new IllegalStateException("not initialized");
    }
    try {
      keyStoreLock.lock();

      String alias = getAlias(certificateTypeId);

      return alias != null && (entries.containsKey(alias) || keyStore.isKeyEntry(alias));
    } finally {
      keyStoreLock.unlock();
    }
  }

  @Override
  public Entry get(NodeId certificateTypeId) throws Exception {
    if (!initialized.get()) {
      throw new IllegalStateException("not initialized");
    }

    try {
      keyStoreLock.lock();

      String alias = getAlias(certificateTypeId);

      if (alias != null) {
        if (entries.containsKey(alias)) {
          return entries.get(alias);
        }

        if (!keyStore.isKeyEntry(alias)) {
          return null;
        }

        Key key = keyStore.getKey(alias, settings.getAliasPassword.apply(alias));
        return loadEntry(alias, key);
      } else {
        return null;
      }
    } finally {
      keyStoreLock.unlock();
    }
  }

  @Override
  public synchronized Entry remove(NodeId certificateTypeId) throws Exception {
    if (!initialized.get()) {
      throw new IllegalStateException("not initialized");
    }

    try {
      keyStoreLock.lock();

      String alias = getAlias(certificateTypeId);

      if (alias != null) {
        reloadBeforeWrite();

        char[] password = settings.getAliasPassword.apply(alias);

        KeyStore.Entry entry = keyStore.getEntry(alias, new KeyStore.PasswordProtection(password));

        if (entry instanceof KeyStore.PrivateKeyEntry privateKeyEntry) {
          keyStore.deleteEntry(alias);
          entries.remove(alias);

          try {
            storeKeyStore();
          } catch (Exception e) {
            restoreEntry(alias, entry, password, e);
            throw e;
          }

          return new Entry(
              privateKeyEntry.getPrivateKey(),
              (X509Certificate[]) privateKeyEntry.getCertificateChain());
        } else {
          return null;
        }
      } else {
        return null;
      }
    } finally {
      keyStoreLock.unlock();
    }
  }

  @Override
  public void set(NodeId certificateTypeId, Entry entry) throws Exception {
    if (!initialized.get()) {
      throw new IllegalStateException("not initialized");
    }

    try {
      keyStoreLock.lock();

      String alias = getAlias(certificateTypeId);

      if (alias == null) {
        return;
      }

      reloadBeforeWrite();

      char[] password = settings.getAliasPassword.apply(alias);

      KeyStore.Entry previousEntry =
          keyStore.isKeyEntry(alias)
              ? keyStore.getEntry(alias, new KeyStore.PasswordProtection(password))
              : null;

      keyStore.setKeyEntry(alias, entry.privateKey, password, entry.certificateChain);

      try {
        storeKeyStore();
      } catch (Exception e) {
        restoreEntry(alias, previousEntry, password, e);
        throw e;
      }

      entries.put(alias, entry);
    } finally {
      keyStoreLock.unlock();
    }
  }

  /**
   * Get the alias to use when accessing certificates of type {@code certificateTypeId}.
   *
   * @param certificateTypeId the {@link NodeId} of the certificate type.
   * @return the alias to use when accessing certificates of type {@code certificateTypeId}, or
   *     {@code null} if the certificate type is not supported.
   */
  protected @Nullable String getAlias(NodeId certificateTypeId) {
    String suffix = ALIAS_SUFFIXES.get(certificateTypeId);

    return suffix != null ? settings.aliasPrefix + suffix : certificateTypeId.toParseableString();
  }

  /**
   * Load configured certificate entries into memory.
   *
   * @throws Exception if an error occurs while loading the entries.
   */
  protected void loadEntries() throws Exception {
    for (NodeId certificateTypeId : getPreloadedCertificateTypeIds()) {
      get(certificateTypeId);
    }
  }

  /**
   * Get the certificate type IDs to load when this store is initialized or reloaded.
   *
   * @return the certificate type IDs to load eagerly.
   */
  protected List<NodeId> getPreloadedCertificateTypeIds() {
    return List.copyOf(ALIAS_SUFFIXES.keySet());
  }

  private Entry loadEntry(String alias, Key key) throws Exception {
    Certificate[] certificateChain = keyStore.getCertificateChain(alias);

    if (key instanceof PrivateKey privateKey && certificateChain != null) {
      X509Certificate[] x509CertificateChain =
          Arrays.stream(certificateChain)
              .map(c -> (X509Certificate) c)
              .toArray(X509Certificate[]::new);

      var entry = new Entry(privateKey, x509CertificateChain);

      entries.putIfAbsent(alias, entry);

      return entry;
    } else {
      return null;
    }
  }

  /**
   * Re-read the on-disk KeyStore immediately before a mutation.
   *
   * <p>This method must be called while {@link #keyStoreLock} is held. Replacing the in-memory
   * KeyStore with the fresh snapshot preserves every alias already committed by another owner,
   * including entries whose passwords are not available to this store.
   */
  private void reloadBeforeWrite() throws Exception {
    Path keyStorePath = resolveKeyStorePath();

    // A missing file has nothing to merge; storeKeyStore() recreates it from memory.
    if (Files.exists(keyStorePath)) {
      keyStore = loadKeyStore(keyStorePath);

      // Cached entries may be stale relative to the fresh snapshot. get() refills the cache
      // lazily rather than loading every alias here, so an unreadable foreign entry under one of
      // this store's aliases cannot fail an unrelated write.
      entries.clear();
    }
  }

  private KeyStore loadKeyStore(Path keyStorePath) throws Exception {
    KeyStore loaded = KeyStore.getInstance("pkcs12");

    try (var inputStream = new FileInputStream(keyStorePath.toFile())) {
      loaded.load(inputStream, settings.getKeyStorePassword.get());
    }

    return loaded;
  }

  /**
   * Write the KeyStore to a temporary file in the same directory and then move it into place,
   * replacing any existing file.
   *
   * <p>Opening the KeyStore file directly would truncate it before the new contents have been
   * written, so a failure part way through the write would leave behind a KeyStore with no keys in
   * it.
   *
   * @throws Exception if an error occurs while writing the KeyStore.
   */
  private void storeKeyStore() throws Exception {
    Path keyStorePath = resolveKeyStorePath();
    Path tempPath = Files.createTempFile(keyStorePath.getParent(), ".keystore", ".tmp");

    try {
      copyPosixFilePermissions(keyStorePath, tempPath);

      try (var outputStream = new FileOutputStream(tempPath.toFile())) {
        keyStore.store(outputStream, settings.getKeyStorePassword.get());

        // Force the contents to disk before the move, so a crash can't leave the moved-into-place
        // file holding nothing.
        outputStream.getFD().sync();
      }

      Files.move(
          tempPath,
          keyStorePath,
          StandardCopyOption.REPLACE_EXISTING,
          StandardCopyOption.ATOMIC_MOVE);
    } catch (Exception e) {
      try {
        Files.deleteIfExists(tempPath);
      } catch (IOException ex) {
        e.addSuppressed(ex);
      }

      throw e;
    }
  }

  /**
   * Resolve the path to write the KeyStore to, following symbolic links so that an existing link is
   * updated in place rather than replaced by a regular file.
   *
   * @return the absolute, link-resolved path of the KeyStore file.
   * @throws IOException if an error occurs while resolving the path.
   */
  private Path resolveKeyStorePath() throws IOException {
    Path keyStorePath = settings.keyStorePath.toAbsolutePath();

    return Files.exists(keyStorePath) ? keyStorePath.toRealPath() : keyStorePath;
  }

  /**
   * Copy the POSIX permissions of {@code from} onto {@code to}, if {@code from} exists and the file
   * system supports them.
   *
   * <p>Temporary files are created readable only by their owner, so without this the KeyStore would
   * lose any permissions the user had configured every time it was replaced.
   *
   * @param from the file to read permissions from.
   * @param to the file to apply them to.
   * @throws IOException if an error occurs while reading or applying the permissions.
   */
  private static void copyPosixFilePermissions(Path from, Path to) throws IOException {
    if (Files.exists(from)
        && from.getFileSystem().supportedFileAttributeViews().contains("posix")) {

      Files.setPosixFilePermissions(to, Files.getPosixFilePermissions(from));
    }
  }

  /**
   * Restore {@code previousEntry} under {@code alias} after a failed write, so the in-memory
   * KeyStore does not diverge from the file on disk.
   *
   * <p>The {@code entries} cache needs no equivalent treatment: {@link #get(NodeId)} falls back to
   * the KeyStore and repopulates it.
   *
   * @param alias the alias to restore.
   * @param previousEntry the entry that was present before the write, or {@code null} if there was
   *     none.
   * @param password the password protecting {@code alias}.
   * @param cause the failure being recovered from, which any failure to restore is attached to.
   */
  private void restoreEntry(
      String alias, KeyStore.@Nullable Entry previousEntry, char[] password, Exception cause) {

    try {
      if (previousEntry != null) {
        keyStore.setEntry(alias, previousEntry, new KeyStore.PasswordProtection(password));
      } else {
        keyStore.deleteEntry(alias);
      }
    } catch (KeyStoreException e) {
      cause.addSuppressed(e);
    }
  }

  private void configureWatchService(File keyStoreFile) throws IOException {
    Path keyStorePath = keyStoreFile.toPath().toAbsolutePath().normalize();
    watchService = FileSystems.getDefault().newWatchService();

    WatchedFile configured = watchFile(keyStorePath);
    WatchedFile target = watchFile(keyStorePath.toRealPath());
    watchThread = new Thread(() -> watchForChanges(configured, target));
    watchThread.setName("milo-key-store-watcher");
    watchThread.setDaemon(true);
    watchThread.start();
  }

  private WatchedFile watchFile(Path path) throws IOException {
    // Atomic replacement is reported as ENTRY_CREATE. ENTRY_DELETE keeps the current target
    // watch active across a temporary disappearance while its replacement is being installed.
    WatchKey key =
        path.getParent()
            .register(
                watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY,
                StandardWatchEventKinds.ENTRY_DELETE);
    return new WatchedFile(path, key);
  }

  private void watchForChanges(WatchedFile configured, WatchedFile initialTarget) {
    WatchedFile target = initialTarget;
    while (true) {
      WatchKey key;
      try {
        key = watchService.take();
      } catch (ClosedWatchServiceException e) {
        return;
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        return;
      }

      List<WatchEvent<?>> events = key.pollEvents();
      if (configured.matches(key, events) || target.matches(key, events)) {
        try {
          Path resolved = configured.path().toRealPath();
          if (!resolved.equals(target.path()) || !target.key().isValid()) {
            WatchedFile replacement = watchFile(resolved);
            // Directory registrations share keys. Cancelling the old target must not cancel the
            // configured-link watch or a replacement target in that same directory.
            if (target.key() != configured.key() && target.key() != replacement.key()) {
              target.key().cancel();
            }
            target = replacement;
          }
        } catch (ClosedWatchServiceException e) {
          return;
        } catch (IOException e) {
          // Preserve the previous target watch while the file is missing or temporarily
          // unreadable; its recreation can then trigger another resolution and reload.
          logger.warn("Error resolving watched KeyStore at {}", configured.path(), e);
        }
        reload(configured.path());
      }

      // Reset every delivered key, including obsolete target keys already cancelled above.
      if (!key.reset() && (key == configured.key() || key == target.key())) {
        logger.warn("KeyStore watch directory is no longer available: {}", key.watchable());
        if (!configured.key().isValid() && !target.key().isValid()) {
          return;
        }
      }
    }
  }

  private record WatchedFile(Path path, WatchKey key) {
    boolean matches(WatchKey signalled, List<WatchEvent<?>> events) {
      return signalled == key
          && events.stream().anyMatch(event -> isKeyStoreEvent(event, path.getParent(), path));
    }
  }

  /**
   * Determine whether {@code event} refers to the KeyStore file.
   *
   * <p>A {@link WatchEvent} delivered by a directory watch carries a context relative to the
   * watched directory, so it has to be resolved against that directory rather than against the
   * working directory.
   *
   * @param event the {@link WatchEvent} to examine.
   * @param watchedDirectory the directory the event was delivered for.
   * @param keyStorePath the path of the KeyStore file.
   * @return {@code true} if the KeyStore file may have changed.
   */
  static boolean isKeyStoreEvent(WatchEvent<?> event, Path watchedDirectory, Path keyStorePath) {

    if (event.kind() == StandardWatchEventKinds.OVERFLOW) {
      // Events were dropped, and there is no way to tell whether the KeyStore was among them.
      return true;
    }

    return event.context() instanceof Path context
        && watchedDirectory.resolve(context).equals(keyStorePath);
  }

  /**
   * Re-read the KeyStore file and repopulate the entries loaded from it.
   *
   * <p>The file is read into a new {@link KeyStore} that replaces the current one only once it has
   * loaded, so a KeyStore that is unreadable, or is still being written, leaves the one in use
   * untouched.
   *
   * @param keyStorePath the path of the KeyStore file.
   */
  void reload(Path keyStorePath) {
    try {
      keyStoreLock.lock();
      try {
        keyStore = loadKeyStore(keyStorePath);

        entries.clear();
        loadEntries();
      } finally {
        keyStoreLock.unlock();
      }

      logger.debug("Reloaded KeyStore at {}", keyStorePath);
    } catch (Exception e) {
      logger.warn("Error reloading KeyStore at {}", keyStorePath, e);
    }
  }

  /**
   * Create and {@link #initialize()} a new {@link KeyStoreCertificateStore} instance.
   *
   * @param settings the {@link Settings} to use.
   * @return an initialized {@link KeyStoreCertificateStore} instance.
   * @throws Exception if an error occurs while initializing the {@link KeyStoreCertificateStore}.
   */
  public static KeyStoreCertificateStore createAndInitialize(Settings settings) throws Exception {
    var store = new KeyStoreCertificateStore(settings);
    store.initialize();
    return store;
  }

  // Keep this as a field-based class for source compatibility with existing callers.
  @SuppressWarnings("ClassCanBeRecord")
  public static class Settings {
    public final Path keyStorePath;
    public final Supplier<char[]> getKeyStorePassword;
    public final Function<String, char[]> getAliasPassword;
    public final String aliasPrefix;
    public final boolean watchForChanges;

    public Settings(
        Path keyStorePath,
        Supplier<char[]> getKeyStorePassword,
        Function<String, char[]> getAliasPassword) {

      this(keyStorePath, getKeyStorePassword, getAliasPassword, false);
    }

    public Settings(
        Path keyStorePath,
        Supplier<char[]> getKeyStorePassword,
        Function<String, char[]> getAliasPassword,
        boolean watchForChanges) {

      this(
          keyStorePath,
          getKeyStorePassword,
          getAliasPassword,
          DEFAULT_ALIAS_PREFIX,
          watchForChanges);
    }

    /**
     * Create settings with a custom prefix for standard certificate type aliases.
     *
     * @param keyStorePath the path of the PKCS#12 KeyStore.
     * @param getKeyStorePassword the supplier for the KeyStore password.
     * @param getAliasPassword the function that supplies each entry password.
     * @param aliasPrefix the prefix for standard application certificate aliases.
     */
    public Settings(
        Path keyStorePath,
        Supplier<char[]> getKeyStorePassword,
        Function<String, char[]> getAliasPassword,
        String aliasPrefix) {

      this(keyStorePath, getKeyStorePassword, getAliasPassword, aliasPrefix, false);
    }

    /**
     * Create settings with a custom alias prefix and optional external-change watching.
     *
     * <p>Watching makes completed external writes visible to reads. It does not serialize writes by
     * separate store instances or processes.
     *
     * @param keyStorePath the path of the PKCS#12 KeyStore.
     * @param getKeyStorePassword the supplier for the KeyStore password.
     * @param getAliasPassword the function that supplies each entry password.
     * @param aliasPrefix the prefix for standard application certificate aliases.
     * @param watchForChanges whether to watch the KeyStore file for external changes.
     */
    public Settings(
        Path keyStorePath,
        Supplier<char[]> getKeyStorePassword,
        Function<String, char[]> getAliasPassword,
        String aliasPrefix,
        boolean watchForChanges) {

      this.keyStorePath = keyStorePath;
      this.getKeyStorePassword = getKeyStorePassword;
      this.getAliasPassword = getAliasPassword;
      this.aliasPrefix = Objects.requireNonNull(aliasPrefix, "aliasPrefix");
      this.watchForChanges = watchForChanges;
    }
  }
}
