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
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.security.Key;
import java.security.KeyStore;
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

      File keyStoreFile = settings.keyStorePath.toFile();

      if (keyStoreFile.exists()) {
        keyStore.load(new FileInputStream(keyStoreFile), settings.getKeyStorePassword.get());

        try {
          keyStoreLock.lock();

          loadEntries();
        } finally {
          keyStoreLock.unlock();
        }
      } else {
        keyStore.load(null, settings.getKeyStorePassword.get());

        keyStore.store(new FileOutputStream(keyStoreFile), settings.getKeyStorePassword.get());
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
        KeyStore.Entry entry =
            keyStore.getEntry(
                alias, new KeyStore.PasswordProtection(settings.getAliasPassword.apply(alias)));

        if (entry instanceof KeyStore.PrivateKeyEntry privateKeyEntry) {
          keyStore.deleteEntry(alias);
          entries.remove(alias);

          keyStore.store(
              new FileOutputStream(settings.keyStorePath.toFile()),
              settings.getKeyStorePassword.get());

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

      keyStore.setKeyEntry(
          alias, entry.privateKey, settings.getAliasPassword.apply(alias), entry.certificateChain);

      keyStore.store(
          new FileOutputStream(settings.keyStorePath.toFile()), settings.getKeyStorePassword.get());

      entries.put(alias, entry);
    } finally {
      keyStoreLock.unlock();
    }
  }

  /**
   * Get the alias to use when accessing certificates of type {@code certificateTypeId}.
   *
   * @param certificateTypeId the {@link NodeId} of the certificate type.
   * @return the alias to use when accessing certificates of type {@code certificateTypeId}.
   */
  protected String getAlias(NodeId certificateTypeId) {
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
