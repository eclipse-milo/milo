/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.sks;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.ServerSocket;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.pubsub.security.InMemoryKeyCredentialStore;
import org.eclipse.milo.opcua.sdk.pubsub.security.SecurityKeySet;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.MemoryTrustListManager;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Rung-1 integration test: the pull provider against an embedded stub SKS — real GetEndpoints
 * resolution, SignAndEncrypt filtering, certificate trust in both directions, anonymous and
 * username identities, marshalling, failover, tolerance fallback, and error-code semantics.
 */
class SksSecurityKeyProviderIntegrationTest {

  private static final String CLIENT_APP_URI = "urn:eclipse:milo:test:sks-pull-client";

  private static StubSksServer sks;
  private static KeyPair clientKeyPair;
  private static X509Certificate clientCertificate;
  private static MemoryTrustListManager clientTrustList;

  @BeforeAll
  static void startStubSks() throws Exception {
    clientKeyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    clientCertificate =
        new SelfSignedCertificateBuilder(clientKeyPair)
            .setCommonName("SKS Pull Client")
            .setApplicationUri(CLIENT_APP_URI)
            .build();

    sks = StubSksServer.create(freePort(), clientCertificate);

    clientTrustList = new MemoryTrustListManager();
    clientTrustList.addTrustedCertificate(sks.getCertificate());
  }

  @AfterAll
  static void stopStubSks() throws Exception {
    if (sks != null) {
      sks.close();
    }
  }

  @Test
  void pullsKeysWithAnonymousIdentity() throws Exception {
    try (SksSecurityKeyProvider provider =
        providerBuilder().keyServices(List.of(serverEntry(sks.getDiscoveryUrl()))).build()) {

      SecurityKeySet keySet =
          provider
              .getKeys(StubSksServer.SECURITY_GROUP_ID, uint(0), uint(1))
              .get(30, TimeUnit.SECONDS);

      assertEquals(StubSksServer.KEY_SET_POLICY_URI, keySet.securityPolicyUri());
      assertEquals(uint(1), keySet.firstTokenId());
      assertEquals(1, keySet.keys().size());
      assertEquals(StubSksServer.KEY_DATA_LENGTH, keySet.keys().get(0).length());
      assertEquals(Duration.ofSeconds(30), keySet.timeToNextKey());
      assertEquals(Duration.ofMinutes(1), keySet.keyLifetime());
    }
  }

  @Test
  void pullsKeysWithUsernameIdentity() throws Exception {
    EndpointDescription entry =
        serverEntry(
            sks.getDiscoveryUrl(),
            new UserTokenPolicy("username", UserTokenType.UserName, null, null, null));

    var credentialStore = new InMemoryKeyCredentialStore();
    credentialStore.put(
        StubSksServer.APPLICATION_URI,
        StubSksServer.USERNAME,
        StubSksServer.PASSWORD.toCharArray());

    try (SksSecurityKeyProvider provider =
        providerBuilder().keyServices(List.of(entry)).keyCredentialStore(credentialStore).build()) {

      SecurityKeySet keySet =
          provider
              .getKeys(StubSksServer.SECURITY_GROUP_ID, uint(0), uint(1))
              .get(30, TimeUnit.SECONDS);

      assertEquals(uint(1), keySet.firstTokenId());
    }
  }

  @Test
  void toleranceFallbackResolvesViaEndpointUrl() throws Exception {
    // Non-conformant entry shape seen in the wild: filled endpointUrl, empty discoveryUrls.
    var entry =
        new EndpointDescription(
            sks.getDiscoveryUrl(),
            new ApplicationDescription(
                StubSksServer.APPLICATION_URI,
                null,
                LocalizedText.NULL_VALUE,
                ApplicationType.Server,
                null,
                null,
                null),
            ByteString.NULL_VALUE,
            MessageSecurityMode.SignAndEncrypt,
            null,
            null,
            null,
            ubyte(0));

    try (SksSecurityKeyProvider provider = providerBuilder().keyServices(List.of(entry)).build()) {

      SecurityKeySet keySet =
          provider
              .getKeys(StubSksServer.SECURITY_GROUP_ID, uint(0), uint(1))
              .get(30, TimeUnit.SECONDS);

      assertEquals(uint(1), keySet.firstTokenId());
    }
  }

  @Test
  void unknownSecurityGroupIsFatalWithoutFailover() throws Exception {
    // Two entries naming the same SKS: Bad_NotFound must NOT fail over to the second one.
    try (SksSecurityKeyProvider provider =
        providerBuilder()
            .keyServices(
                List.of(serverEntry(sks.getDiscoveryUrl()), serverEntry(sks.getDiscoveryUrl())))
            .build()) {

      int invocationsBefore = sks.getInvocationCount().get();

      ExecutionException e =
          assertThrows(
              ExecutionException.class,
              () -> provider.getKeys("NoSuchGroup", uint(0), uint(1)).get(30, TimeUnit.SECONDS));

      UaException cause = assertInstanceOf(UaException.class, e.getCause());
      assertEquals(StatusCodes.Bad_NotFound, cause.getStatusCode().value());
      assertEquals(invocationsBefore + 1, sks.getInvocationCount().get());
    }
  }

  @Test
  void accessDeniedGroupFailsTheFetchWithDiagnostic() throws Exception {
    try (SksSecurityKeyProvider provider =
        providerBuilder().keyServices(List.of(serverEntry(sks.getDiscoveryUrl()))).build()) {

      ExecutionException e =
          assertThrows(
              ExecutionException.class,
              () ->
                  provider
                      .getKeys(StubSksServer.DENIED_GROUP_ID, uint(0), uint(1))
                      .get(30, TimeUnit.SECONDS));

      UaException cause = assertInstanceOf(UaException.class, e.getCause());
      // A single entry: the Bad_UserAccessDenied failure exhausts the array and surfaces in the
      // aggregate diagnostic, naming the identity token type used.
      assertTrue(cause.getMessage().contains("Anonymous"), cause.getMessage());
    }
  }

  @Test
  void failsOverPastUnreachableEntry() throws Exception {
    String unreachableUrl = "opc.tcp://localhost:" + freePort() + "/sks";

    try (SksSecurityKeyProvider provider =
        providerBuilder()
            .keyServices(List.of(serverEntry(unreachableUrl), serverEntry(sks.getDiscoveryUrl())))
            .build()) {

      SecurityKeySet keySet =
          provider
              .getKeys(StubSksServer.SECURITY_GROUP_ID, uint(0), uint(1))
              .get(60, TimeUnit.SECONDS);

      assertEquals(uint(1), keySet.firstTokenId());
    }
  }

  private static SksSecurityKeyProvider.Builder providerBuilder() {
    return SksSecurityKeyProvider.builder()
        .keyPair(clientKeyPair)
        .certificate(clientCertificate)
        .applicationUri(CLIENT_APP_URI)
        .trustListManager(clientTrustList)
        .requestTimeout(Duration.ofSeconds(15));
  }

  private static EndpointDescription serverEntry(
      String discoveryUrl, UserTokenPolicy... userIdentityTokens) {

    return new EndpointDescription(
        null,
        new ApplicationDescription(
            StubSksServer.APPLICATION_URI,
            null,
            LocalizedText.NULL_VALUE,
            ApplicationType.Server,
            null,
            null,
            new String[] {discoveryUrl}),
        ByteString.NULL_VALUE,
        MessageSecurityMode.SignAndEncrypt,
        null,
        userIdentityTokens.length == 0 ? null : userIdentityTokens,
        null,
        ubyte(0));
  }

  private static int freePort() throws Exception {
    try (var serverSocket = new ServerSocket(0)) {
      return serverSocket.getLocalPort();
    }
  }
}
