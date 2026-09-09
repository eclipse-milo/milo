/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.session;

import static org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig.USER_TOKEN_POLICY_ANONYMOUS;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.Provider;
import java.security.Security;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.identity.AnonymousProvider;
import org.eclipse.milo.opcua.sdk.client.identity.IdentityProvider;
import org.eclipse.milo.opcua.sdk.client.identity.UsernameProvider;
import org.eclipse.milo.opcua.sdk.client.identity.X509IdentityProvider;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfigBuilder;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.DefaultClientCertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.MemoryTrustListManager;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.parallel.Isolated;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

@Isolated("Changes the JVM security provider order")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RsaSessionIntegrationTest {

  private static final Provider BC = new BouncyCastleProvider();
  private static final List<SecurityPolicy> RSA_POLICIES =
      List.of(
          SecurityPolicy.Basic128Rsa15,
          SecurityPolicy.Basic256,
          SecurityPolicy.Basic256Sha256,
          SecurityPolicy.Aes128_Sha256_RsaOaep,
          SecurityPolicy.Aes256_Sha256_RsaPss);
  private static final List<MessageSecurityMode> MODES =
      List.of(MessageSecurityMode.Sign, MessageSecurityMode.SignAndEncrypt);
  private static final List<UserTokenType> IDENTITIES =
      List.of(UserTokenType.Anonymous, UserTokenType.UserName, UserTokenType.Certificate);
  private TestServer testServer;
  private OpcUaServer server;
  private String endpointUrl;
  private DefaultClientCertificateValidator certificateValidator;
  private Provider previousBc;

  @BeforeAll
  void startServer() throws Exception {
    previousBc = Security.getProvider("BC");
    Security.removeProvider("BC");
    testServer = TestServer.create(builder -> builder.setEndpoints(rsaEndpoints(builder)));
    server = testServer.getServer();
    EndpointConfig endpoint = server.getConfig().getEndpoints().iterator().next();
    endpointUrl = endpoint.getEndpointUrl();
    var trustList = new MemoryTrustListManager();
    trustList.addTrustedCertificate(endpoint.getCertificate());
    certificateValidator =
        new DefaultClientCertificateValidator(trustList, new MemoryCertificateQuarantine());
    server.startup().get(10, TimeUnit.SECONDS);
    assertNull(Security.getProvider("BC"), "server setup must not register BC");
  }

  @AfterAll
  void stopServer() throws Exception {
    try {
      if (server != null) {
        server.shutdown().get(10, TimeUnit.SECONDS);
      }
    } finally {
      Security.removeProvider("BC");
      if (previousBc != null) {
        Security.addProvider(previousBc);
      }
    }
  }

  // Username and certificate token policies leave the security policy URI null so the token
  // inherits the endpoint's RSA policy, unlike the Basic256Sha256-pinned OpcUaServerConfig
  // constants.
  private static Set<EndpointConfig> rsaEndpoints(OpcUaServerConfigBuilder builder) {
    EndpointConfig base = builder.build().getEndpoints().iterator().next();
    Set<EndpointConfig> endpoints = new LinkedHashSet<>();
    for (SecurityPolicy policy : RSA_POLICIES) {
      for (MessageSecurityMode mode : MODES) {
        endpoints.add(
            EndpointConfig.newBuilder()
                .setBindAddress("localhost")
                .setHostname("localhost")
                .setBindPort(base.getBindPort())
                .setPath("/test")
                .setTransportProfile(base.getTransportProfile())
                .setCertificate(base.getCertificate())
                .setSecurityPolicy(policy)
                .setSecurityMode(mode)
                .addTokenPolicies(
                    USER_TOKEN_POLICY_ANONYMOUS,
                    new UserTokenPolicy("username", UserTokenType.UserName, null, null, null),
                    new UserTokenPolicy("certificate", UserTokenType.Certificate, null, null, null))
                .build());
      }
    }
    return endpoints;
  }

  @BeforeEach
  @AfterEach
  void removeBouncyCastle() {
    Security.removeProvider("BC");
  }

  // A successful authenticated Read requires OpenSecureChannel, CreateSession, and ActivateSession
  // to agree on the RSA policy. Token policies inherit the endpoint policy, including RSA-PSS.
  @ParameterizedTest
  @MethodSource("rsaConfigurations")
  void establishesChannelActivatesSessionAndReads(
      SecurityPolicy policy, MessageSecurityMode mode, UserTokenType identity, boolean bcFirst)
      throws Exception {
    if (bcFirst) {
      Security.insertProviderAt(BC, 1);
    }
    IdentityProvider provider =
        switch (identity) {
          case Anonymous -> AnonymousProvider.INSTANCE;
          case UserName -> new UsernameProvider("user1", "password");
          case Certificate ->
              new X509IdentityProvider(
                  testServer.getIdentityCertificate1().certificate(),
                  testServer.getIdentityCertificate1().keyPair().getPrivate());
          default -> throw new IllegalArgumentException(identity.name());
        };
    OpcUaClient client = createClient(policy, mode, provider);
    try {
      client.connectAsync().get(10, TimeUnit.SECONDS);
      assertNotNull(client.getSession());
      assertEquals(policy.getUri(), client.getConfig().getEndpoint().getSecurityPolicyUri());
      assertEquals(mode, client.getConfig().getEndpoint().getSecurityMode());
      DataValue value =
          client.readValue(0, TimestampsToReturn.Neither, NodeIds.Server_ServerStatus_State);
      assertTrue(value.statusCode().isGood());
      assertNotNull(value.value().value());
      assertEquals(
          bcFirst,
          Security.getProvider("BC") != null,
          "connection must not change BC registration");
    } finally {
      client.disconnectAsync().get(10, TimeUnit.SECONDS);
    }
  }

  // A wrong password must fail activation even when an anonymous token policy is also advertised.
  // The legacy encrypted-token validator reports Bad_IdentityTokenInvalid for bad credentials.
  @ParameterizedTest
  @EnumSource(
      value = MessageSecurityMode.class,
      names = {"Sign", "SignAndEncrypt"})
  void rejectsIncorrectPasswordWithRsaPss(MessageSecurityMode mode) throws Exception {
    OpcUaClient client =
        createClient(
            SecurityPolicy.Aes256_Sha256_RsaPss, mode, new UsernameProvider("user1", "incorrect"));
    try {
      ExecutionException failure =
          assertThrows(
              ExecutionException.class, () -> client.connectAsync().get(10, TimeUnit.SECONDS));
      assertEquals(
          StatusCodes.Bad_IdentityTokenInvalid,
          UaException.extract(failure).orElseThrow().getStatusCode().value());
    } finally {
      client.disconnectAsync().get(10, TimeUnit.SECONDS);
    }
  }

  private OpcUaClient createClient(
      SecurityPolicy policy, MessageSecurityMode mode, IdentityProvider identity)
      throws UaException {
    return OpcUaClient.create(
        endpointUrl,
        endpoints ->
            endpoints.stream()
                .filter(
                    e ->
                        policy.getUri().equals(e.getSecurityPolicyUri())
                            && e.getSecurityMode() == mode)
                .findFirst(),
        transportBuilder -> {},
        builder ->
            builder
                .setCertificateIdentity(
                    testServer.getClientKeyPair(), testServer.getClientCertificateChain())
                .setCertificateValidator(certificateValidator)
                .setIdentityProvider(identity)
                .setRequestTimeout(uint(5_000)));
  }

  private static Stream<Arguments> rsaConfigurations() {
    List<Arguments> configurations = new ArrayList<>();
    for (SecurityPolicy policy : RSA_POLICIES) {
      for (MessageSecurityMode mode : MODES) {
        for (UserTokenType identity : IDENTITIES) {
          for (boolean bcFirst : List.of(false, true)) {
            configurations.add(Arguments.of(policy, mode, identity, bcFirst));
          }
        }
      }
    }
    return configurations.stream();
  }
}
