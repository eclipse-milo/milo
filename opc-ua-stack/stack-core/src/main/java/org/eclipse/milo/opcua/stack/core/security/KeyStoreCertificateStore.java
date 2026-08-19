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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.function.Supplier;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeyStoreCertificateStore implements CertificateStore, Closeable {

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

      keyStore = KeyStore.getInstance("pkcs12");

      File keyStoreFile = settings.keyStorePath.toAbsolutePath().toFile();

      if (keyStoreFile.exists()) {
        try (var inputStream = new FileInputStream(keyStoreFile)) {
          keyStore.load(inputStream, settings.getKeyStorePassword.get());
        }

        try {
          keyStoreLock.lock();

          loadEntries();
        } finally {
          keyStoreLock.unlock();
        }
      } else {
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

      return alias != null && (entries.containsKey(alias) || keyStore.containsAlias(alias));
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

        Key key = keyStore.getKey(alias, settings.getAliasPassword.apply(alias));
        Certificate[] certificateChain = keyStore.getCertificateChain(alias);

        if (key instanceof PrivateKey && certificateChain != null) {
          X509Certificate[] x509CertificateChain =
              Arrays.stream(certificateChain)
                  .map(c -> (X509Certificate) c)
                  .toArray(X509Certificate[]::new);

          var entry = new Entry((PrivateKey) key, x509CertificateChain);

          entries.putIfAbsent(alias, entry);

          return entry;
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
  public synchronized Entry remove(NodeId certificateTypeId) throws Exception {
    if (!initialized.get()) {
      throw new IllegalStateException("not initialized");
    }

    try {
      keyStoreLock.lock();

      String alias = getAlias(certificateTypeId);

      if (alias != null) {
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
    if (certificateTypeId.equals(NodeIds.RsaSha256ApplicationCertificateType)) {
      return "server-rsa-sha256";
    } else {
      return certificateTypeId.toParseableString();
    }
  }

  /**
   * Call {@link #get(NodeId)} for each of the supported certificate types to pre-emptively load
   * them into memory.
   *
   * @throws Exception if an error occurs while loading the entries.
   */
  protected void loadEntries() throws Exception {
    // Try to get each of the certificate types we support, pre-emptively loading them into
    // `cache` for faster subsequent access.

    get(NodeIds.RsaSha256ApplicationCertificateType);
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
    watchService = FileSystems.getDefault().newWatchService();

    WatchKey watchKey =
        keyStoreFile
            .toPath()
            .getParent()
            .register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

    watchThread =
        new Thread(
            new Runnable() {
              @Override
              public void run() {
                while (true) {
                  try {
                    WatchKey key = watchService.take();
                    if (key == watchKey) {
                      key.pollEvents().forEach(this::processWatchEvent);
                    }
                  } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                  }
                }
              }

              private void processWatchEvent(WatchEvent<?> event) {
                if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY
                    && event.context() instanceof Path p) {

                  if (p.toAbsolutePath().equals(keyStoreFile.toPath().toAbsolutePath())) {
                    try {
                      keyStoreLock.lock();

                      entries.clear();
                      loadEntries();
                    } catch (Exception ignored) {
                      // ignored
                    } finally {
                      keyStoreLock.unlock();
                    }
                  }
                }
              }
            });

    watchThread.setName("milo-key-store-watcher");
    watchThread.setDaemon(true);
    watchThread.start();
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

  public static class Settings {
    public final Path keyStorePath;
    public final Supplier<char[]> getKeyStorePassword;
    public final Function<String, char[]> getAliasPassword;
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

      this.keyStorePath = keyStorePath;
      this.getKeyStorePassword = getKeyStorePassword;
      this.getAliasPassword = getAliasPassword;
      this.watchForChanges = watchForChanges;
    }
  }
}
