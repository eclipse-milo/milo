/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.server;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.identity.AnonymousProvider;
import org.eclipse.milo.opcua.sdk.client.identity.IdentityProvider;
import org.eclipse.milo.opcua.sdk.client.identity.UsernameProvider;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.SecurityGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.security.PubSubSecurityPolicy;
import org.eclipse.milo.opcua.sdk.server.RoleMapper;
import org.eclipse.milo.opcua.sdk.server.identity.Identity;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.CallResponse;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;

/**
 * The K17 authorization matrix over a REAL secure channel and session: a started {@link
 * SksTestServer} hosts the K15 SKS face, and {@link OpcUaClient} connections at {@code None},
 * {@code Sign}, and {@code SignAndEncrypt} call {@code GetSecurityKeys} end-to-end.
 *
 * <p>Rows covered (WP-T4 §5): A1/A2 — a None or Sign channel is denied {@code
 * Bad_SecurityModeInsufficient}, exercising the K17.1 status propagation (the ns0 GetSecurityKeys
 * node carries {@code AccessRestrictions = SigningRequired|EncryptionRequired}, so the ACCESS
 * CONTROLLER denies before the handler; the handler's own channel check is its backstop). A5 — an
 * encrypted caller with no RoleMapper configured is served keys for a group without explicit
 * RolePermissions. A3 — with a RoleMapper fixture granting the well-known SKS pull roles, the
 * mapped user is served keys over the encrypted channel while an unmapped user is denied {@code
 * Bad_UserAccessDenied}.
 *
 * <p>The direct-invoke matrix (A6-A10 and friends) lives in {@link SksServerFaceTest}; this class
 * proves the channel-facing rows the mocked-session tests cannot.
 */
class SksMethodAuthorizationIntegrationTest {

  private static final String CLIENT_APPLICATION_URI = "urn:eclipse:milo:test:sks-client";

  private static final String OPEN_GROUP = "open-group";

  private static final Duration TIMEOUT = Duration.ofSeconds(10);

  /** A1 + A2 + A5, against a server with NO RoleMapper. */
  @Test
  void channelSecurityModeIsEnforcedEndToEnd() throws Exception {
    try (SksTestServer testServer = SksTestServer.create(null)) {
      ServerPubSub serverPubSub = attachSksFace(testServer);
      try {
        KeyPair clientKeyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
        X509Certificate clientCertificate = clientCertificate(clientKeyPair);
        testServer.trustClientCertificate(clientCertificate);

        // A1: None channel — denied with the K17.1-propagated status, not a generic Bad
        OpcUaClient noneClient =
            connect(
                testServer,
                SecurityPolicy.None,
                MessageSecurityMode.None,
                new AnonymousProvider(),
                null,
                null);
        try {
          CallMethodResult result = getSecurityKeys(noneClient, OPEN_GROUP);
          assertEquals(
              new StatusCode(StatusCodes.Bad_SecurityModeInsufficient), result.getStatusCode());
        } finally {
          noneClient.disconnect();
        }

        // A2: Sign channel — signing alone is insufficient, GetSecurityKeys needs encryption
        OpcUaClient signClient =
            connect(
                testServer,
                SecurityPolicy.Basic256Sha256,
                MessageSecurityMode.Sign,
                new AnonymousProvider(),
                clientKeyPair,
                clientCertificate);
        try {
          CallMethodResult result = getSecurityKeys(signClient, OPEN_GROUP);
          assertEquals(
              new StatusCode(StatusCodes.Bad_SecurityModeInsufficient), result.getStatusCode());
        } finally {
          signClient.disconnect();
        }

        // A5: SignAndEncrypt channel, no RoleMapper, group without explicit RolePermissions —
        // Good, and the keys arrive over the encrypted channel
        OpcUaClient encryptedClient =
            connect(
                testServer,
                SecurityPolicy.Basic256Sha256,
                MessageSecurityMode.SignAndEncrypt,
                new AnonymousProvider(),
                clientKeyPair,
                clientCertificate);
        try {
          CallMethodResult result = getSecurityKeys(encryptedClient, OPEN_GROUP);
          assertEquals(StatusCode.GOOD, result.getStatusCode());
          assertServedKeys(result);
        } finally {
          encryptedClient.disconnect();
        }
      } finally {
        serverPubSub.close();
      }
    }
  }

  /** A3 (grant) plus the unmapped-user deny, against a server WITH a RoleMapper fixture. */
  @Test
  void roleMapperGrantsTheSksPullRolesEndToEnd() throws Exception {
    // the K17 test-fixture RoleMapper (a shipped RoleMapper is a non-goal): the "keys" user is
    // granted the well-known SKS roles, every other identity gets none
    RoleMapper roleMapper =
        identity -> {
          if (identity instanceof Identity.UsernameIdentity username
              && SksTestServer.USER_WITH_KEYS_ROLE.equals(username.getUsername())) {
            return List.of(
                NodeIds.WellKnownRole_SecurityKeyServerAccess,
                NodeIds.WellKnownRole_SecurityKeyServerAdmin);
          }
          return List.of();
        };

    try (SksTestServer testServer = SksTestServer.create(roleMapper)) {
      ServerPubSub serverPubSub = attachSksFace(testServer);
      try {
        KeyPair clientKeyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
        X509Certificate clientCertificate = clientCertificate(clientKeyPair);
        testServer.trustClientCertificate(clientCertificate);

        // A3: the mapped user is served keys over the encrypted channel
        OpcUaClient authorizedClient =
            connect(
                testServer,
                SecurityPolicy.Basic256Sha256,
                MessageSecurityMode.SignAndEncrypt,
                new UsernameProvider(SksTestServer.USER_WITH_KEYS_ROLE, SksTestServer.PASSWORD),
                clientKeyPair,
                clientCertificate);
        try {
          CallMethodResult result = getSecurityKeys(authorizedClient, OPEN_GROUP);
          assertEquals(StatusCode.GOOD, result.getStatusCode());
          assertServedKeys(result);
        } finally {
          authorizedClient.disconnect();
        }

        // with a mapper configured, a user holding NO SKS role is denied — even encrypted
        OpcUaClient unauthorizedClient =
            connect(
                testServer,
                SecurityPolicy.Basic256Sha256,
                MessageSecurityMode.SignAndEncrypt,
                new UsernameProvider(SksTestServer.USER_WITHOUT_ROLES, SksTestServer.PASSWORD),
                clientKeyPair,
                clientCertificate);
        try {
          CallMethodResult result = getSecurityKeys(unauthorizedClient, OPEN_GROUP);
          assertEquals(new StatusCode(StatusCodes.Bad_UserAccessDenied), result.getStatusCode());
        } finally {
          unauthorizedClient.disconnect();
        }
      } finally {
        serverPubSub.close();
      }
    }
  }

  // region fixtures + helpers

  private static ServerPubSub attachSksFace(SksTestServer testServer) throws Exception {
    PubSubConfig config =
        PubSubConfig.builder()
            .securityGroup(
                SecurityGroupConfig.builder(OPEN_GROUP).keyLifeTime(Duration.ofMinutes(10)).build())
            .build();

    ServerPubSub serverPubSub =
        ServerPubSub.attach(
            testServer.getServer(),
            config,
            ServerPubSubOptions.builder().securityKeyServerEnabled(true).build());

    serverPubSub.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS);

    return serverPubSub;
  }

  private static X509Certificate clientCertificate(KeyPair keyPair) throws Exception {
    SelfSignedCertificateBuilder builder = new SelfSignedCertificateBuilder(keyPair);
    builder.setCommonName("SksAuthorizationTestClient");
    builder.setApplicationUri(CLIENT_APPLICATION_URI);
    return builder.build();
  }

  private static OpcUaClient connect(
      SksTestServer testServer,
      SecurityPolicy securityPolicy,
      MessageSecurityMode securityMode,
      IdentityProvider identityProvider,
      @Nullable KeyPair keyPair,
      @Nullable X509Certificate certificate)
      throws UaException {

    OpcUaClient client =
        OpcUaClient.create(
            testServer.getEndpointUrl(),
            endpoints ->
                endpoints.stream()
                    .filter(
                        e ->
                            Objects.equals(e.getSecurityPolicyUri(), securityPolicy.getUri())
                                && e.getSecurityMode() == securityMode)
                    .findFirst(),
            transportConfigBuilder -> {},
            clientConfigBuilder -> {
              clientConfigBuilder
                  .setApplicationName(LocalizedText.english("sks authorization test client"))
                  .setApplicationUri(CLIENT_APPLICATION_URI)
                  .setIdentityProvider(identityProvider)
                  .setRequestTimeout(uint(5_000));
              if (keyPair != null && certificate != null) {
                clientConfigBuilder
                    .setKeyPair(keyPair)
                    .setCertificate(certificate)
                    .setCertificateChain(new X509Certificate[] {certificate});
              }
            });

    client.connect();

    return client;
  }

  private static CallMethodResult getSecurityKeys(OpcUaClient client, String securityGroupId)
      throws UaException {

    CallResponse response =
        client.call(
            List.of(
                new CallMethodRequest(
                    NodeIds.PublishSubscribe,
                    NodeIds.PublishSubscribe_GetSecurityKeys,
                    new Variant[] {
                      new Variant(securityGroupId), new Variant(uint(0)), new Variant(uint(0))
                    })));

    CallMethodResult[] results = response.getResults();
    assertNotNull(results);
    assertEquals(1, results.length);
    return results[0];
  }

  /** The Good result actually carries key material in the K15 output shape. */
  private static void assertServedKeys(CallMethodResult result) {
    Variant[] outputs = result.getOutputArguments();
    assertNotNull(outputs);
    assertEquals(5, outputs.length);

    // no policy URI configured on the group: the store's default policy is PubSub-Aes256-CTR
    assertEquals(PubSubSecurityPolicy.PubSubAes256Ctr.getUri(), outputs[0].getValue());

    ByteString[] keys = (ByteString[]) outputs[2].getValue();
    assertNotNull(keys);
    assertTrue(keys.length >= 1);
    assertEquals(68, keys[0].length());
  }

  // endregion
}
