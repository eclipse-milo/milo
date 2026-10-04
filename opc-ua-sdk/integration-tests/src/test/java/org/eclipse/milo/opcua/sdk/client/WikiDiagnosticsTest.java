/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfigLimits;
import org.eclipse.milo.opcua.sdk.server.model.objects.ServerDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.test.TestPortAllocator;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.channel.WiresharkKeyLogWriter;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.DefaultClientCertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.MemoryTrustListManager;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.eclipse.milo.opcua.stack.core.util.validation.ValidationCheck;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.io.TempDir;

/**
 * Local encrypted traffic and diagnostics for the Wiki; optional paths support capture validation.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Timeout(30)
public class WikiDiagnosticsTest {

  @TempDir Path directory;
  private TestServer fixture;
  private OpcUaServer server;
  private String discoveryUrl;
  private CertificateValidator validator;

  @BeforeAll
  void startServer() throws Exception {
    int port = Integer.getInteger("wiki.port", TestPortAllocator.allocatePort());
    KeyPair serverKeys = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    String applicationUri = "urn:eclipse:milo:wiki:diagnostics-server";
    X509Certificate certificate =
        new SelfSignedCertificateBuilder(serverKeys)
            .setCommonName("Wiki diagnostics server")
            .setApplicationUri(applicationUri)
            .addDnsName("localhost")
            .addIpAddress("127.0.0.1")
            .build();
    fixture =
        TestServer.create(
            port,
            new OpcUaServerConfigLimits() {},
            builder -> {
              OpcUaServerConfig defaults = builder.build();
              try {
                defaults
                    .getCertificateManager()
                    .getDefaultApplicationGroup()
                    .orElseThrow()
                    .updateCertificate(
                        NodeIds.RsaSha256ApplicationCertificateType,
                        serverKeys,
                        new X509Certificate[] {certificate});
              } catch (Exception e) {
                throw new IllegalStateException(e);
              }
              EndpointConfig.Builder endpoint =
                  EndpointConfig.newBuilder()
                      .setBindAddress("127.0.0.1")
                      .setHostname("localhost")
                      .setBindPort(port)
                      .setPath("/wiki-diagnostics")
                      .setCertificate(certificate)
                      .addTokenPolicy(OpcUaServerConfig.USER_TOKEN_POLICY_ANONYMOUS);
              builder
                  .setApplicationUri(applicationUri)
                  .setEndpoints(
                      Set.of(
                          endpoint.copy().build(),
                          endpoint
                              .copy()
                              .setSecurityPolicy(SecurityPolicy.Basic256Sha256)
                              .setSecurityMode(MessageSecurityMode.SignAndEncrypt)
                              .build()));
            });
    server = fixture.getServer();
    var trust = new MemoryTrustListManager();
    trust.addTrustedCertificate(certificate);
    validator =
        new DefaultClientCertificateValidator(
            trust, ValidationCheck.ALL_OPTIONAL_CHECKS, new MemoryCertificateQuarantine());
    discoveryUrl = "opc.tcp://localhost:" + port + "/wiki-diagnostics";
    server.startup().get(10, TimeUnit.SECONDS);
  }

  @AfterAll
  void stopServer() throws Exception {
    if (server != null) server.shutdown().get(10, TimeUnit.SECONDS);
  }

  // A real encrypted service exchange must generate a complete record without printing its secrets.
  @Test
  void encryptedReadProducesUsableKeyLog() throws Exception {
    String configured = System.getProperty("wiki.keylog");
    Path keyLogPath = configured == null ? directory.resolve("opcua.keys") : Path.of(configured);
    assertFalse(Files.exists(keyLogPath), "capture validation must use a fresh key-log path");
    DataValue value =
        readWithKeyLog(
            keyLogPath,
            discoveryUrl,
            fixture.getClientKeyPair(),
            fixture.getClientCertificateChain(),
            validator);
    assertTrue(value.statusCode().isGood());
    assertTrue(value.value().value() instanceof DateTime);
    List<String> lines = Files.readAllLines(keyLogPath);
    assertTrue(lines.size() >= 6);
    assertEquals(0, lines.size() % 6);
    // Assert only validity booleans, so assertion output never exposes the key material.
    assertTrue(lines.get(0).matches("client_iv_[0-9]+_[0-9]+: [A-F0-9]{32}"));
    assertTrue(lines.get(1).matches("client_key_[0-9]+_[0-9]+: [A-F0-9]{64}"));
    assertTrue(lines.get(2).matches("client_siglen_[0-9]+_[0-9]+: 32"));
    assertTrue(lines.get(3).matches("server_iv_[0-9]+_[0-9]+: [A-F0-9]{32}"));
    assertTrue(lines.get(4).matches("server_key_[0-9]+_[0-9]+: [A-F0-9]{64}"));
    assertTrue(lines.get(5).matches("server_siglen_[0-9]+_[0-9]+: 32"));
  }

  // Logging stays opt-in even when both endpoints establish an encrypted channel successfully.
  @Test
  void encryptedChannelDoesNotInstallADefaultKeyLogger() throws Exception {
    OpcUaClient client = newSecureClient();
    try {
      assertTrue(client.getConfig().getSecurityKeysListener().isEmpty());
      assertTrue(server.getConfig().getSecurityKeysListener().isEmpty());
      client.connectAsync().get(10, TimeUnit.SECONDS);
      assertTrue(
          client
              .readValue(0, TimestampsToReturn.Neither, NodeIds.Server_ServerStatus_State)
              .statusCode()
              .isGood());
    } finally {
      client.disconnectAsync().get(10, TimeUnit.SECONDS);
    }
  }

  // EnabledFlag controls data availability; encryption and role restrictions remain independent.
  @Test
  void diagnosticsAvailabilityDoesNotGrantAnonymousSecurityDiagnostics() throws Exception {
    OpcUaClient client = newSecureClient();
    ServerDiagnosticsTypeNode diagnostics =
        (ServerDiagnosticsTypeNode)
            server
                .getAddressSpaceManager()
                .getManagedNode(NodeIds.Server_ServerDiagnostics)
                .orElseThrow();
    try {
      client.connectAsync().get(10, TimeUnit.SECONDS);
      assertFalse(diagnostics.getEnabledFlag());
      DataValue disabled =
          client.readValue(
              0,
              TimestampsToReturn.Neither,
              NodeIds.Server_ServerDiagnostics_ServerDiagnosticsSummary_CurrentSessionCount);
      assertEquals(StatusCodes.Bad_OutOfService, disabled.statusCode().getValue());
      // wiki:diagnostics-enable:start
      diagnostics.setEnabledFlag(true);
      // wiki:diagnostics-enable:end
      DataValue enabled =
          client.readValue(
              0,
              TimestampsToReturn.Neither,
              NodeIds.Server_ServerDiagnostics_ServerDiagnosticsSummary_CurrentSessionCount);
      assertTrue(enabled.statusCode().isGood());
      assertTrue(((UInteger) enabled.value().value()).longValue() >= 1);
      DataValue restricted =
          client.readValue(
              0,
              TimestampsToReturn.Neither,
              NodeIds
                  .Server_ServerDiagnostics_SessionsDiagnosticsSummary_SessionSecurityDiagnosticsArray);
      assertEquals(StatusCodes.Bad_UserAccessDenied, restricted.statusCode().getValue());
    } finally {
      diagnostics.setEnabledFlag(false);
      client.disconnectAsync().get(10, TimeUnit.SECONDS);
    }
    OpcUaClient unsecured = OpcUaClient.create(discoveryUrl);
    try {
      unsecured.connectAsync().get(10, TimeUnit.SECONDS);
      DataValue restricted =
          unsecured.readValue(
              0,
              TimestampsToReturn.Neither,
              NodeIds
                  .Server_ServerDiagnostics_SessionsDiagnosticsSummary_SessionSecurityDiagnosticsArray);
      assertEquals(StatusCodes.Bad_SecurityModeInsufficient, restricted.statusCode().getValue());
    } finally {
      unsecured.disconnectAsync().get(10, TimeUnit.SECONDS);
    }
  }

  private OpcUaClient newSecureClient() throws Exception {
    return OpcUaClient.create(
        discoveryUrl,
        endpoints ->
            endpoints.stream()
                .filter(
                    e -> SecurityPolicy.Basic256Sha256.getUri().equals(e.getSecurityPolicyUri()))
                .filter(e -> e.getSecurityMode() == MessageSecurityMode.SignAndEncrypt)
                .findFirst(),
        transport -> {},
        config ->
            config
                .setCertificateIdentity(
                    fixture.getClientKeyPair(), fixture.getClientCertificateChain())
                .setCertificateValidator(validator));
  }

  // wiki:key-log:start
  static DataValue readWithKeyLog(
      Path keyLogPath,
      String discoveryUrl,
      KeyPair keyPair,
      X509Certificate[] certificateChain,
      CertificateValidator validator)
      throws Exception {
    try (var writer = new WiresharkKeyLogWriter(keyLogPath)) {
      OpcUaClient client =
          OpcUaClient.create(
              discoveryUrl,
              endpoints ->
                  endpoints.stream()
                      .filter(
                          e ->
                              SecurityPolicy.Basic256Sha256.getUri()
                                  .equals(e.getSecurityPolicyUri()))
                      .filter(e -> e.getSecurityMode() == MessageSecurityMode.SignAndEncrypt)
                      .findFirst(),
              transport -> {},
              config ->
                  config
                      .setCertificateIdentity(keyPair, certificateChain)
                      .setCertificateValidator(validator)
                      .setSecurityKeysListener(writer));
      try {
        client.connectAsync().get(10, TimeUnit.SECONDS);
        return client.readValue(
            0, TimestampsToReturn.Both, NodeIds.Server_ServerStatus_CurrentTime);
      } finally {
        client.disconnectAsync().get(10, TimeUnit.SECONDS);
      }
    }
  }
  // wiki:key-log:end
}
