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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.security.CertificateIdentity;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateGroup;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.MemoryTrustListManager;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

/**
 * End-to-end coverage for a client whose only certificate configuration is a single {@link
 * DefaultCertificateGroup}, connecting to the shared {@link TestServer}.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OpcUaClientCertificateGroupTest {

  private TestServer testServer;
  private OpcUaServer server;

  @BeforeAll
  void startServer() throws Exception {
    testServer = TestServer.create();
    server = testServer.getServer();
    server.startup().get(5, TimeUnit.SECONDS);
  }

  @AfterAll
  void stopServer() throws Exception {
    server.shutdown().get(5, TimeUnit.SECONDS);
  }

  // DefaultCertificateGroup.forIdentity is the migration path from the removed
  // setKeyPair/setCertificate builder methods. A group of one must carry the client through
  // OpenSecureChannel, CreateSession and ActivateSession on a SignAndEncrypt endpoint, with the
  // ApplicationUri derived from the certificate rather than configured; the server rejects a
  // mismatch with Bad_CertificateUriInvalid.
  @Test
  void groupOfOneConnectsOverBasic256Sha256SignAndEncrypt() throws Exception {
    DefaultCertificateGroup certificateGroup =
        DefaultCertificateGroup.forIdentity(
            testServer.getClientKeyPair(),
            testServer.getClientCertificateChain(),
            new MemoryTrustListManager(),
            new MemoryCertificateQuarantine(),
            new CertificateValidator.InsecureCertificateValidator());

    OpcUaClient client =
        OpcUaClient.create(
            endpointUrl(),
            endpoints ->
                endpoints.stream()
                    .filter(
                        e ->
                            SecurityPolicy.Basic256Sha256.getUri().equals(e.getSecurityPolicyUri()))
                    .filter(e -> e.getSecurityMode() == MessageSecurityMode.SignAndEncrypt)
                    .findFirst(),
            transport -> {},
            config ->
                config
                    .setApplicationName(
                        LocalizedText.english("eclipse milo certificate group test client"))
                    .setRequestTimeout(uint(5_000))
                    .setCertificateGroup(certificateGroup));

    client.connect();
    try {
      CertificateIdentity identity =
          client.getCertificateIdentity(SecurityPolicy.Basic256Sha256.getProfile()).orElseThrow();
      assertEquals(testServer.getClientCertificate(), identity.certificate());

      DataValue value =
          client.readValue(0.0, TimestampsToReturn.Neither, NodeIds.Server_ServerStatus_State);

      assertTrue(value.statusCode().isGood(), () -> "read failed: " + value.statusCode());
      assertNotNull(value.value().value());
    } finally {
      client.disconnect();
    }
  }

  // setCertificateIdentity is the path for a client with only a key pair and certificate on hand.
  // It must reach an active session on a SignAndEncrypt endpoint without the caller building any
  // trust material, with the same certificate-derived ApplicationUri as the group path.
  @Test
  void fixedIdentityConnectsOverBasic256Sha256SignAndEncrypt() throws Exception {
    OpcUaClient client =
        OpcUaClient.create(
            endpointUrl(),
            endpoints ->
                endpoints.stream()
                    .filter(
                        e ->
                            SecurityPolicy.Basic256Sha256.getUri().equals(e.getSecurityPolicyUri()))
                    .filter(e -> e.getSecurityMode() == MessageSecurityMode.SignAndEncrypt)
                    .findFirst(),
            transport -> {},
            config ->
                config
                    .setApplicationName(
                        LocalizedText.english("eclipse milo fixed identity test client"))
                    .setRequestTimeout(uint(5_000))
                    .setCertificateIdentity(
                        testServer.getClientKeyPair(), testServer.getClientCertificateChain()));

    client.connect();
    try {
      CertificateIdentity identity =
          client.getCertificateIdentity(SecurityPolicy.Basic256Sha256.getProfile()).orElseThrow();
      assertEquals(testServer.getClientCertificate(), identity.certificate());

      DataValue value =
          client.readValue(0.0, TimestampsToReturn.Neither, NodeIds.Server_ServerStatus_State);

      assertTrue(value.statusCode().isGood(), () -> "read failed: " + value.statusCode());
      assertNotNull(value.value().value());
    } finally {
      client.disconnect();
    }
  }

  private String endpointUrl() {
    return server.getConfig().getEndpoints().iterator().next().getEndpointUrl();
  }
}
