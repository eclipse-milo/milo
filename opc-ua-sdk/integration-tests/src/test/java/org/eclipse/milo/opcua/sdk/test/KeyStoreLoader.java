/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;
import java.util.regex.Pattern;
import org.eclipse.milo.opcua.sdk.server.util.HostnameUtil;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeyStoreLoader {

  private static final Pattern IP_ADDR_PATTERN =
      Pattern.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

  private static final String SERVER_ALIAS = "server-ai";
  private static final String CLIENT_ALIAS = "client-ai";
  private static final char[] PASSWORD = "password".toCharArray();

  private static final Logger LOGGER = LoggerFactory.getLogger(KeyStoreLoader.class);

  private X509Certificate[] serverCertificateChain;
  private X509Certificate serverCertificate;
  private KeyPair serverKeyPair;

  private X509Certificate[] clientCertificateChain;
  private X509Certificate clientCertificate;
  private KeyPair clientKeyPair;

  /**
   * Loads or creates server and client keystores with self-signed certificates.
   *
   * <p>If keystores do not exist at the expected paths, they will be created with new self-signed
   * certificates. Server certificates include hostname and IP address SANs.
   *
   * @param baseDir the directory containing or where keystores will be created.
   * @return this KeyStoreLoader instance for method chaining.
   * @throws IllegalArgumentException if baseDir is null.
   * @throws Exception if keystore operations fail.
   */
  public KeyStoreLoader load(File baseDir) throws Exception {
    if (baseDir == null) {
      throw new IllegalArgumentException("baseDir cannot be null");
    }

    KeyStore serverKeyStore =
        loadOrCreateKeyStore(
            baseDir.toPath().resolve("test-server.pfx").toFile(),
            SERVER_ALIAS,
            "Eclipse Milo Test Server",
            "urn:eclipse:milo:test:server:" + UUID.randomUUID(),
            true);

    loadServerCredentials(serverKeyStore);

    KeyStore clientKeyStore =
        loadOrCreateKeyStore(
            baseDir.toPath().resolve("test-client.pfx").toFile(),
            CLIENT_ALIAS,
            "Eclipse Milo Test Client",
            "urn:eclipse:milo:test:client:" + UUID.randomUUID(),
            false);

    loadClientCredentials(clientKeyStore);

    return this;
  }

  /**
   * Loads an existing PKCS12 keystore or creates a new one if it doesn't exist.
   *
   * @param keyStoreFile the keystore file to load or create.
   * @param alias the alias for the key entry.
   * @param commonName the common name for the certificate.
   * @param applicationUri the OPC UA application URI.
   * @param includeHostnames whether to include hostname and IP address SANs.
   * @return the loaded or created KeyStore.
   * @throws Exception if keystore operations fail.
   */
  private KeyStore loadOrCreateKeyStore(
      File keyStoreFile,
      String alias,
      String commonName,
      String applicationUri,
      boolean includeHostnames)
      throws Exception {

    KeyStore keyStore = KeyStore.getInstance("PKCS12");

    LOGGER.debug("Loading KeyStore at {}", keyStoreFile);

    if (!keyStoreFile.exists()) {
      createNewKeyStore(
          keyStore, keyStoreFile, alias, commonName, applicationUri, includeHostnames);
    } else {
      try (FileInputStream fis = new FileInputStream(keyStoreFile)) {
        keyStore.load(fis, PASSWORD);
      }
    }

    return keyStore;
  }

  /**
   * Creates a new PKCS12 keystore with a self-signed certificate.
   *
   * @param keyStore the empty KeyStore instance to populate.
   * @param keyStoreFile the file where the keystore will be saved.
   * @param alias the alias for the key entry.
   * @param commonName the common name for the certificate.
   * @param applicationUri the OPC UA application URI.
   * @param includeHostnames whether to include hostname and IP address SANs.
   * @throws Exception if keystore creation or storage fails.
   */
  private void createNewKeyStore(
      KeyStore keyStore,
      File keyStoreFile,
      String alias,
      String commonName,
      String applicationUri,
      boolean includeHostnames)
      throws Exception {

    keyStore.load(null, PASSWORD);

    KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);

    SelfSignedCertificateBuilder builder =
        new SelfSignedCertificateBuilder(keyPair)
            .setCommonName(commonName)
            .setOrganization("digitalpetri")
            .setOrganizationalUnit("dev")
            .setLocalityName("Folsom")
            .setStateName("CA")
            .setCountryCode("US")
            .setApplicationUri(applicationUri);

    if (includeHostnames) {
      addHostnamesToCertificate(builder);
    }

    X509Certificate certificate = builder.build();

    keyStore.setKeyEntry(
        alias, keyPair.getPrivate(), PASSWORD, new X509Certificate[] {certificate});

    try (FileOutputStream fos = new FileOutputStream(keyStoreFile)) {
      keyStore.store(fos, PASSWORD);
    }
  }

  /**
   * Loads server certificate, certificate chain, and key pair from the keystore.
   *
   * @param keyStore the keystore containing server credentials.
   * @throws Exception if credential extraction fails.
   */
  private void loadServerCredentials(KeyStore keyStore) throws Exception {
    Key privateKey = keyStore.getKey(SERVER_ALIAS, PASSWORD);
    if (privateKey instanceof PrivateKey) {
      serverCertificate = (X509Certificate) keyStore.getCertificate(SERVER_ALIAS);
      serverCertificateChain = getCertificateChain(keyStore, SERVER_ALIAS);
      serverKeyPair = new KeyPair(serverCertificate.getPublicKey(), (PrivateKey) privateKey);
    }
  }

  /**
   * Loads client certificate, certificate chain, and key pair from the keystore.
   *
   * @param keyStore the keystore containing client credentials.
   * @throws Exception if credential extraction fails.
   */
  private void loadClientCredentials(KeyStore keyStore) throws Exception {
    Key privateKey = keyStore.getKey(CLIENT_ALIAS, PASSWORD);
    if (privateKey instanceof PrivateKey) {
      clientCertificate = (X509Certificate) keyStore.getCertificate(CLIENT_ALIAS);
      clientCertificateChain = getCertificateChain(keyStore, CLIENT_ALIAS);
      clientKeyPair = new KeyPair(clientCertificate.getPublicKey(), (PrivateKey) privateKey);
    }
  }

  /**
   * Adds all available hostnames and IP addresses as SANs to the certificate.
   *
   * @param builder the certificate builder to configure.
   */
  private static void addHostnamesToCertificate(SelfSignedCertificateBuilder builder) {
    var hostnames = new HashSet<String>();
    hostnames.add(HostnameUtil.getHostname());
    hostnames.addAll(HostnameUtil.getHostnames("0.0.0.0", false));

    for (String hostname : hostnames) {
      if (IP_ADDR_PATTERN.matcher(hostname).matches()) {
        builder.addIpAddress(hostname);
      } else {
        builder.addDnsName(hostname);
      }
    }
  }

  /**
   * Extracts the certificate chain for the given alias from the keystore.
   *
   * @param keyStore the keystore containing the certificate chain.
   * @param alias the alias of the key entry.
   * @return the certificate chain as an array of X509Certificate.
   * @throws Exception if chain extraction fails.
   */
  private static X509Certificate[] getCertificateChain(KeyStore keyStore, String alias)
      throws Exception {

    return Arrays.stream(keyStore.getCertificateChain(alias))
        .map(X509Certificate.class::cast)
        .toArray(X509Certificate[]::new);
  }

  /**
   * Returns the server certificate.
   *
   * @return the server X509 certificate.
   */
  public X509Certificate getServerCertificate() {
    return serverCertificate;
  }

  /**
   * Returns the server certificate chain.
   *
   * @return the server certificate chain.
   */
  public X509Certificate[] getServerCertificateChain() {
    return serverCertificateChain;
  }

  /**
   * Returns the server key pair.
   *
   * @return the server key pair.
   */
  public KeyPair getServerKeyPair() {
    return serverKeyPair;
  }

  /**
   * Returns the client certificate.
   *
   * @return the client X509 certificate.
   */
  public X509Certificate getClientCertificate() {
    return clientCertificate;
  }

  /**
   * Returns the client certificate chain.
   *
   * @return the client certificate chain.
   */
  public X509Certificate[] getClientCertificateChain() {
    return clientCertificateChain;
  }

  /**
   * Returns the client key pair.
   *
   * @return the client key pair.
   */
  public KeyPair getClientKeyPair() {
    return clientKeyPair;
  }
}
