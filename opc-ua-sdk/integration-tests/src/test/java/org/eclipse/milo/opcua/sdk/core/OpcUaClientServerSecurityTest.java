/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.core;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.File;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfigBuilder;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.test.KeyStoreLoader;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.security.DefaultClientCertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.MemoryTrustListManager;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class OpcUaClientServerSecurityTest {

  private OpcUaServer server;
  private KeyPair clientKeyPair;
  private X509Certificate clientCertificate;
  private X509Certificate[] clientCertificateChain;
  private MemoryTrustListManager trustListManager;

  @BeforeEach
  public void setup() throws Exception {
    TestServer testServer = TestServer.create();
    server = testServer.getServer();
    server.startup().get();

    File securityTempDir = new File(System.getProperty("java.io.tmpdir"), "security");
    KeyStoreLoader loader = new KeyStoreLoader().load(securityTempDir);

    clientKeyPair = loader.getClientKeyPair();
    clientCertificate = loader.getClientCertificate();
    clientCertificateChain = loader.getClientCertificateChain();

    trustListManager = new MemoryTrustListManager();
    trustListManager.addTrustedCertificate(loader.getServerCertificate());

    server
        .getConfig()
        .getCertificateManager()
        .getCertificateGroups()
        .forEach(group -> group.getTrustListManager().addTrustedCertificate(clientCertificate));
  }

  @AfterEach
  public void tearDown() throws Exception {
    server.shutdown().get(2, TimeUnit.SECONDS);
  }

  @ParameterizedTest
  @MethodSource("securityConfigurations")
  public void testClientConnectsWithSecurityConfiguration(
      SecurityPolicy securityPolicy, MessageSecurityMode messageSecurityMode) {

    assertDoesNotThrow(
        () -> {
          OpcUaClient client =
              OpcUaClient.create(
                  server.getConfig().getEndpoints().iterator().next().getEndpointUrl(),
                  endpoints ->
                      endpoints.stream()
                          .filter(
                              e ->
                                  Objects.equals(e.getSecurityPolicyUri(), securityPolicy.getUri())
                                      && Objects.equals(e.getSecurityMode(), messageSecurityMode))
                          .findFirst(),
                  transportConfigBuilder -> {},
                  this::configureClient);

          try {
            client.connect();

            assertDoesNotThrow(client::getSession);
          } finally {
            client.disconnect();
          }
        },
        String.format(
            "Failed to connect with SecurityPolicy=%s and MessageSecurityMode=%s",
            securityPolicy, messageSecurityMode));
  }

  private void configureClient(OpcUaClientConfigBuilder configBuilder) {
    var certificateValidator =
        new DefaultClientCertificateValidator(trustListManager, new MemoryCertificateQuarantine());

    String applicationUri =
        CertificateUtil.getSanUri(clientCertificate)
            .orElse("urn:eclipse:milo:test:security:client");

    configBuilder
        .setApplicationName(LocalizedText.english("eclipse milo security test client"))
        .setApplicationUri(applicationUri)
        .setKeyPair(clientKeyPair)
        .setCertificate(clientCertificate)
        .setCertificateChain(clientCertificateChain)
        .setCertificateValidator(certificateValidator);
  }

  private static Stream<Arguments> securityConfigurations() {
    return Stream.of(
        // SecurityPolicy.None with MessageSecurityMode.None
        Arguments.of(SecurityPolicy.None, MessageSecurityMode.None),

        // Basic128Rsa15 with Sign and SignAndEncrypt
        Arguments.of(SecurityPolicy.Basic128Rsa15, MessageSecurityMode.Sign),
        Arguments.of(SecurityPolicy.Basic128Rsa15, MessageSecurityMode.SignAndEncrypt),

        // Basic256 with Sign and SignAndEncrypt
        Arguments.of(SecurityPolicy.Basic256, MessageSecurityMode.Sign),
        Arguments.of(SecurityPolicy.Basic256, MessageSecurityMode.SignAndEncrypt),

        // Basic256Sha256 with Sign and SignAndEncrypt
        Arguments.of(SecurityPolicy.Basic256Sha256, MessageSecurityMode.Sign),
        Arguments.of(SecurityPolicy.Basic256Sha256, MessageSecurityMode.SignAndEncrypt),

        // Aes128_Sha256_RsaOaep with Sign and SignAndEncrypt
        Arguments.of(SecurityPolicy.Aes128_Sha256_RsaOaep, MessageSecurityMode.Sign),
        Arguments.of(SecurityPolicy.Aes128_Sha256_RsaOaep, MessageSecurityMode.SignAndEncrypt),

        // Aes256_Sha256_RsaPss with Sign and SignAndEncrypt
        Arguments.of(SecurityPolicy.Aes256_Sha256_RsaPss, MessageSecurityMode.Sign),
        Arguments.of(SecurityPolicy.Aes256_Sha256_RsaPss, MessageSecurityMode.SignAndEncrypt));
  }
}
