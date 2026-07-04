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

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.server.ServerPubSub;
import org.eclipse.milo.opcua.sdk.pubsub.server.ServerPubSubOptions;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.RoleMapper;
import org.eclipse.milo.opcua.sdk.server.identity.AnonymousIdentityValidator;
import org.eclipse.milo.opcua.sdk.server.identity.CompositeValidator;
import org.eclipse.milo.opcua.sdk.server.identity.Identity;
import org.eclipse.milo.opcua.sdk.server.identity.UsernameIdentityValidator;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.security.DefaultApplicationGroup;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateManager;
import org.eclipse.milo.opcua.stack.core.security.DefaultServerCertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateStore;
import org.eclipse.milo.opcua.stack.core.security.MemoryTrustListManager;
import org.eclipse.milo.opcua.stack.core.security.RsaSha256CertificateFactory;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.BuildInfo;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransport;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransportConfig;

/**
 * The SKS harness: a REAL {@link OpcUaServer} with None, Sign, and SignAndEncrypt TCP endpoints and
 * a {@link ServerPubSub} attached with {@code securityKeyServerEnabled(true)}, so {@code
 * GetSecurityKeys} (ns=0;i=15215) is served by the real SKS helper — the real {@code
 * SecurityGroupKeyStore} rotation arithmetic, the real default-posture authorizer, and the real
 * access-controller path in front of it (including the {@code Bad_SecurityModeInsufficient}
 * propagation for the AccessRestrictions the ns0 loader ships on i=15215).
 *
 * <p>Certificate and endpoint machinery is the {@link StubSksServer} shape (itself modeled on
 * integration-tests' {@code TestServer}), extended with a Sign endpoint and a test-fixture {@link
 * RoleMapper} granting:
 *
 * <ul>
 *   <li>{@link #AUTHORIZED_USERNAME} → {@code WellKnownRole_SecurityKeyServerAccess} (i=25603)
 *   <li>{@link #UNAUTHORIZED_USERNAME} and anonymous → no roles ("mapper configured, empty roles" —
 *       the default posture denies key access)
 * </ul>
 */
final class SksTestServer implements AutoCloseable {

  static final String APPLICATION_URI = "urn:eclipse:milo:test:sks-test-server";

  static final String AUTHORIZED_USERNAME = "keyuser";
  static final String AUTHORIZED_PASSWORD = "key-password";
  static final String UNAUTHORIZED_USERNAME = "lockedout";
  static final String UNAUTHORIZED_PASSWORD = "locked-password";

  private final OpcUaServer server;
  private final ServerPubSub serverPubSub;
  private final X509Certificate certificate;
  private final int port;

  private SksTestServer(
      OpcUaServer server, ServerPubSub serverPubSub, X509Certificate certificate, int port) {

    this.server = server;
    this.serverPubSub = serverPubSub;
    this.certificate = certificate;
    this.port = port;
  }

  X509Certificate getCertificate() {
    return certificate;
  }

  String getDiscoveryUrl() {
    return "opc.tcp://localhost:" + port + "/sks";
  }

  @Override
  public void close() throws Exception {
    serverPubSub.close();
    server.shutdown().get(10, TimeUnit.SECONDS);
  }

  /**
   * Create and start a server on {@code port}, trusting {@code trustedClientCertificate}, with the
   * SKS helper serving the SecurityGroups of {@code pubSubConfig}.
   */
  static SksTestServer create(
      int port, X509Certificate trustedClientCertificate, PubSubConfig pubSubConfig)
      throws Exception {

    KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);

    X509Certificate certificate =
        new SelfSignedCertificateBuilder(keyPair)
            .setCommonName("Milo SKS Test Server")
            .setApplicationUri(APPLICATION_URI)
            .addDnsName("localhost")
            .build();

    var trustListManager = new MemoryTrustListManager();
    trustListManager.addTrustedCertificate(trustedClientCertificate);

    var certificateQuarantine = new MemoryCertificateQuarantine();

    var certificateFactory =
        new RsaSha256CertificateFactory() {
          @Override
          protected KeyPair createRsaSha256KeyPair() {
            return keyPair;
          }

          @Override
          protected X509Certificate[] createRsaSha256CertificateChain(KeyPair keyPair) {
            return new X509Certificate[] {certificate};
          }
        };

    var certificateValidator =
        new DefaultServerCertificateValidator(trustListManager, certificateQuarantine);

    var defaultGroup =
        DefaultApplicationGroup.createAndInitialize(
            trustListManager,
            new MemoryCertificateStore(),
            certificateFactory,
            certificateValidator);

    var certificateManager = new DefaultCertificateManager(certificateQuarantine, defaultGroup);

    var usernameIdentityValidator =
        new UsernameIdentityValidator(
            authChallenge ->
                (AUTHORIZED_USERNAME.equals(authChallenge.getUsername())
                        && AUTHORIZED_PASSWORD.equals(authChallenge.getPassword()))
                    || (UNAUTHORIZED_USERNAME.equals(authChallenge.getUsername())
                        && UNAUTHORIZED_PASSWORD.equals(authChallenge.getPassword())));

    // test-fixture RoleMapper: with a mapper configured, the default posture requires the
    // well-known pull roles; only the authorized user carries SecurityKeyServerAccess
    RoleMapper roleMapper =
        identity -> {
          if (identity instanceof Identity.UsernameIdentity usernameIdentity
              && AUTHORIZED_USERNAME.equals(usernameIdentity.getUsername())) {
            return List.of(NodeIds.WellKnownRole_SecurityKeyServerAccess);
          }
          return List.of();
        };

    OpcUaServerConfig serverConfig =
        OpcUaServerConfig.builder()
            .setApplicationUri(APPLICATION_URI)
            .setApplicationName(LocalizedText.english("Milo SKS Test Server"))
            .setProductUri("urn:eclipse:milo:test:sks-test-server:product")
            .setEndpoints(endpointConfigs(certificate, port))
            .setBuildInfo(
                new BuildInfo(
                    "urn:eclipse:milo:test:sks-test-server:product",
                    "eclipse",
                    "milo sks test server",
                    OpcUaServer.SDK_VERSION,
                    "",
                    DateTime.now()))
            .setCertificateManager(certificateManager)
            .setIdentityValidator(
                new CompositeValidator(
                    AnonymousIdentityValidator.INSTANCE, usernameIdentityValidator))
            .setRoleMapper(roleMapper)
            .build();

    var server =
        new OpcUaServer(
            serverConfig,
            transportProfile -> {
              if (transportProfile == TransportProfile.TCP_UASC_UABINARY) {
                return new OpcTcpServerTransport(OpcTcpServerTransportConfig.newBuilder().build());
              } else {
                throw new RuntimeException("unexpected TransportProfile: " + transportProfile);
              }
            });

    server.startup().get();

    ServerPubSub serverPubSub = null;
    try {
      serverPubSub =
          ServerPubSub.attach(
              server,
              pubSubConfig,
              ServerPubSubOptions.builder().securityKeyServerEnabled(true).build());

      serverPubSub.startup().get(10, TimeUnit.SECONDS);
    } catch (Exception e) {
      if (serverPubSub != null) {
        serverPubSub.close();
      }
      server.shutdown().get(10, TimeUnit.SECONDS);
      throw e;
    }

    return new SksTestServer(server, serverPubSub, certificate, port);
  }

  private static Set<EndpointConfig> endpointConfigs(X509Certificate certificate, int port) {
    EndpointConfig.Builder base =
        EndpointConfig.newBuilder()
            .setBindAddress("localhost")
            .setBindPort(port)
            .setHostname("localhost")
            .setPath("/sks")
            .setCertificate(certificate)
            .setTransportProfile(TransportProfile.TCP_UASC_UABINARY)
            .addTokenPolicies(
                OpcUaServerConfig.USER_TOKEN_POLICY_ANONYMOUS,
                OpcUaServerConfig.USER_TOKEN_POLICY_USERNAME);

    EndpointConfig none =
        base.copy()
            .setSecurityPolicy(SecurityPolicy.None)
            .setSecurityMode(MessageSecurityMode.None)
            .build();

    EndpointConfig sign =
        base.copy()
            .setSecurityPolicy(SecurityPolicy.Basic256Sha256)
            .setSecurityMode(MessageSecurityMode.Sign)
            .build();

    EndpointConfig signAndEncrypt =
        base.copy()
            .setSecurityPolicy(SecurityPolicy.Basic256Sha256)
            .setSecurityMode(MessageSecurityMode.SignAndEncrypt)
            .build();

    return Set.of(none, sign, signAndEncrypt);
  }
}
