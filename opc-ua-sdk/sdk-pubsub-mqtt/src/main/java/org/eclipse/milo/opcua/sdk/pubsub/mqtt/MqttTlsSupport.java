/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.mqtt;

import com.hivemq.client.mqtt.MqttClientSslConfig;
import com.hivemq.client.mqtt.MqttClientSslConfigBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import org.eclipse.milo.opcua.sdk.pubsub.config.BrokerSecurityConfig;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.jspecify.annotations.Nullable;

/**
 * Builds the HiveMQ {@link MqttClientSslConfig} from a {@link BrokerSecurityConfig}'s PEM paths.
 *
 * <p>Supported material: X.509 certificates in PEM (or DER) form for the CA certificate and the
 * client certificate chain, and an <b>unencrypted PKCS#8</b> ({@code -----BEGIN PRIVATE KEY-----})
 * private key for the client key. Documented v1 limits: no key passphrase support (encrypted
 * PKCS#8, PKCS#1, and SEC1 keys are rejected with {@code Bad_NotSupported} — convert with {@code
 * openssl pkcs8 -topk8 -nocrypt}), and no cipher-suite or TLS-protocol pinning (the client/Netty
 * defaults apply).
 *
 * <p>{@link BrokerSecurityConfig#isAllowUntrustedCertificates()} installs a trust-everything trust
 * manager and a permissive hostname verifier; otherwise a configured CA certificate becomes the
 * trust anchor, and with neither, the JDK default trust store applies.
 */
final class MqttTlsSupport {

  private static final Pattern PKCS8_PATTERN =
      Pattern.compile("-----BEGIN PRIVATE KEY-----(.*?)-----END PRIVATE KEY-----", Pattern.DOTALL);

  private MqttTlsSupport() {}

  /**
   * Build the {@link MqttClientSslConfig} for a TLS broker connection.
   *
   * @param security the broker security config, or {@code null} when the connection has none (TLS
   *     scheme with JDK default trust).
   * @return the {@link MqttClientSslConfig}.
   * @throws UaException with {@code Bad_ConfigurationError} if only one of client certificate and
   *     client key is configured, {@code Bad_NotSupported} for unsupported key formats, or {@code
   *     Bad_SecurityChecksFailed} if the configured material cannot be read or parsed.
   */
  static MqttClientSslConfig sslConfig(@Nullable BrokerSecurityConfig security) throws UaException {
    MqttClientSslConfigBuilder builder = MqttClientSslConfig.builder();

    if (security != null && security.isAllowUntrustedCertificates()) {
      builder =
          builder
              .trustManagerFactory(InsecureTrustManagerFactory.INSTANCE)
              .hostnameVerifier((hostname, session) -> true);
    } else if (security != null && security.getCaCertificate() != null) {
      builder = builder.trustManagerFactory(trustManagerFactory(security.getCaCertificate()));
    }

    if (security != null) {
      Path clientCertificate = security.getClientCertificate();
      Path clientKey = security.getClientKey();

      if ((clientCertificate == null) != (clientKey == null)) {
        throw new UaException(
            StatusCodes.Bad_ConfigurationError,
            "clientCertificate and clientKey must both be configured for mutual TLS");
      }
      if (clientCertificate != null) {
        builder = builder.keyManagerFactory(keyManagerFactory(clientCertificate, clientKey));
      }
    }

    return builder.build();
  }

  /** A {@link TrustManagerFactory} trusting exactly the certificates in {@code caCertificate}. */
  private static TrustManagerFactory trustManagerFactory(Path caCertificate) throws UaException {
    try {
      Collection<? extends Certificate> certificates = readCertificates(caCertificate);

      KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
      keyStore.load(null, null);
      int i = 0;
      for (Certificate certificate : certificates) {
        keyStore.setCertificateEntry("ca-" + i++, certificate);
      }

      TrustManagerFactory factory =
          TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
      factory.init(keyStore);
      return factory;
    } catch (GeneralSecurityException | IOException e) {
      throw new UaException(
          StatusCodes.Bad_SecurityChecksFailed,
          "failed to build trust material from " + caCertificate,
          e);
    }
  }

  /**
   * A {@link KeyManagerFactory} presenting the certificate chain at {@code clientCertificate} with
   * the private key at {@code clientKey}.
   */
  private static KeyManagerFactory keyManagerFactory(Path clientCertificate, Path clientKey)
      throws UaException {

    PrivateKey privateKey = readPrivateKey(clientKey);

    try {
      Collection<? extends Certificate> chain = readCertificates(clientCertificate);

      KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
      keyStore.load(null, null);
      keyStore.setKeyEntry("client", privateKey, new char[0], chain.toArray(new Certificate[0]));

      KeyManagerFactory factory =
          KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
      factory.init(keyStore, new char[0]);
      return factory;
    } catch (GeneralSecurityException | IOException e) {
      throw new UaException(
          StatusCodes.Bad_SecurityChecksFailed,
          "failed to build key material from " + clientCertificate,
          e);
    }
  }

  private static Collection<? extends Certificate> readCertificates(Path path)
      throws UaException, IOException, GeneralSecurityException {

    Collection<? extends Certificate> certificates;
    try (InputStream in = Files.newInputStream(path)) {
      certificates = CertificateFactory.getInstance("X.509").generateCertificates(in);
    }
    if (certificates.isEmpty()) {
      throw new UaException(
          StatusCodes.Bad_SecurityChecksFailed, "no X.509 certificates found in " + path);
    }
    return certificates;
  }

  /** Read an unencrypted PKCS#8 private key from a PEM file. */
  private static PrivateKey readPrivateKey(Path path) throws UaException {
    String pem;
    try {
      pem = Files.readString(path, StandardCharsets.US_ASCII);
    } catch (IOException e) {
      throw new UaException(
          StatusCodes.Bad_SecurityChecksFailed, "failed to read private key from " + path, e);
    }

    if (pem.contains("BEGIN ENCRYPTED PRIVATE KEY")) {
      throw new UaException(
          StatusCodes.Bad_NotSupported,
          "encrypted private keys are not supported in this version: " + path);
    }
    if (pem.contains("BEGIN RSA PRIVATE KEY") || pem.contains("BEGIN EC PRIVATE KEY")) {
      throw new UaException(
          StatusCodes.Bad_NotSupported,
          ("PKCS#1/SEC1 private keys are not supported in this version; convert to PKCS#8 with"
                  + " 'openssl pkcs8 -topk8 -nocrypt': %s")
              .formatted(path));
    }

    Matcher matcher = PKCS8_PATTERN.matcher(pem);
    if (!matcher.find()) {
      throw new UaException(
          StatusCodes.Bad_SecurityChecksFailed, "no PKCS#8 PRIVATE KEY block found in " + path);
    }

    byte[] der;
    try {
      der = Base64.getMimeDecoder().decode(matcher.group(1).strip());
    } catch (IllegalArgumentException e) {
      throw new UaException(
          StatusCodes.Bad_SecurityChecksFailed, "malformed PEM private key in " + path, e);
    }

    var keySpec = new PKCS8EncodedKeySpec(der);
    for (String algorithm : List.of("RSA", "EC", "Ed25519", "DSA")) {
      try {
        return KeyFactory.getInstance(algorithm).generatePrivate(keySpec);
      } catch (NoSuchAlgorithmException | InvalidKeySpecException ignored) {
        // try the next algorithm
      }
    }

    throw new UaException(
        StatusCodes.Bad_SecurityChecksFailed,
        "unsupported private key algorithm (expected RSA, EC, Ed25519, or DSA): " + path);
  }
}
