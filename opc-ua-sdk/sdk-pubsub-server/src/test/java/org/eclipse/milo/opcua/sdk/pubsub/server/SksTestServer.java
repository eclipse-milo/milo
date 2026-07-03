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

import static org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig.USER_TOKEN_POLICY_ANONYMOUS;
import static org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig.USER_TOKEN_POLICY_USERNAME;

import java.net.ServerSocket;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfigBuilder;
import org.eclipse.milo.opcua.sdk.server.RoleMapper;
import org.eclipse.milo.opcua.sdk.server.identity.AnonymousIdentityValidator;
import org.eclipse.milo.opcua.sdk.server.identity.CompositeValidator;
import org.eclipse.milo.opcua.sdk.server.identity.UsernameIdentityValidator;
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
import org.jspecify.annotations.Nullable;

/**
 * A STARTED {@link OpcUaServer} fixture with real opc.tcp endpoints for the K17 real-channel
 * authorization matrix — the deliberate opposite of {@link TestPubSubServer}, which is never
 * started and has no transports (and therefore cannot host GetSecurityKeys-over-channel tests).
 *
 * <p>Modeled on the integration-tests {@code TestServer} shape but self-contained: the server
 * certificate is generated in-memory with {@link SelfSignedCertificateBuilder} (no keystore
 * loader), endpoints bind an ephemeral port on {@code localhost} at {@code None},
 * Basic256Sha256/{@code Sign}, and Basic256Sha256/{@code SignAndEncrypt}, each with anonymous and
 * username token policies. Two users exist: {@code "keys"} and {@code "nobody"} (password {@code
 * "password"} each) so a test {@link RoleMapper} fixture can grant SKS roles to one and not the
 * other. Client certificates must be registered via {@link #trustClientCertificate} before a
 * secured connect.
 */
final class SksTestServer implements AutoCloseable {

  static final String USER_WITH_KEYS_ROLE = "keys";
  static final String USER_WITHOUT_ROLES = "nobody";
  static final String PASSWORD = "password";

  private static final String APPLICATION_URI = "urn:eclipse:milo:test:sks-server";

  private final OpcUaServer server;
  private final MemoryTrustListManager trustListManager;
  private final String endpointUrl;

  private SksTestServer(
      OpcUaServer server, MemoryTrustListManager trustListManager, String endpointUrl) {
    this.server = server;
    this.trustListManager = trustListManager;
    this.endpointUrl = endpointUrl;
  }

  /**
   * Create and START a server, with {@code roleMapper} configured when non-null.
   *
   * @param roleMapper the {@link RoleMapper} to configure, or {@code null} for the no-RoleMapper
   *     posture.
   */
  static SksTestServer create(@Nullable RoleMapper roleMapper) throws Exception {
    int port = freeTcpPort();

    KeyPair serverKeyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    SelfSignedCertificateBuilder certificateBuilder =
        new SelfSignedCertificateBuilder(serverKeyPair);
    certificateBuilder.setCommonName("SksTestServer");
    certificateBuilder.setApplicationUri(APPLICATION_URI);
    X509Certificate serverCertificate = certificateBuilder.build();

    var trustListManager = new MemoryTrustListManager();
    var certificateStore = new MemoryCertificateStore();
    var certificateQuarantine = new MemoryCertificateQuarantine();

    var certificateFactory =
        new RsaSha256CertificateFactory() {
          @Override
          protected KeyPair createRsaSha256KeyPair() {
            return serverKeyPair;
          }

          @Override
          protected X509Certificate[] createRsaSha256CertificateChain(KeyPair keyPair) {
            return new X509Certificate[] {serverCertificate};
          }
        };

    var certificateValidator =
        new DefaultServerCertificateValidator(trustListManager, certificateQuarantine);

    var defaultGroup =
        DefaultApplicationGroup.createAndInitialize(
            trustListManager, certificateStore, certificateFactory, certificateValidator);

    var certificateManager = new DefaultCertificateManager(certificateQuarantine, defaultGroup);

    UsernameIdentityValidator usernameValidator =
        new UsernameIdentityValidator(
            challenge ->
                (USER_WITH_KEYS_ROLE.equals(challenge.getUsername())
                        || USER_WITHOUT_ROLES.equals(challenge.getUsername()))
                    && PASSWORD.equals(challenge.getPassword()));

    OpcUaServerConfigBuilder configBuilder =
        OpcUaServerConfig.builder()
            .setApplicationUri(APPLICATION_URI)
            .setApplicationName(LocalizedText.english("SKS authorization test server"))
            .setProductUri(APPLICATION_URI)
            .setBuildInfo(
                new BuildInfo(
                    APPLICATION_URI,
                    "eclipse",
                    "sks test server",
                    OpcUaServer.SDK_VERSION,
                    "",
                    DateTime.now()))
            .setEndpoints(endpointConfigs(serverCertificate, port))
            .setCertificateManager(certificateManager)
            .setIdentityValidator(
                new CompositeValidator(AnonymousIdentityValidator.INSTANCE, usernameValidator));

    if (roleMapper != null) {
      configBuilder.setRoleMapper(roleMapper);
    }

    var server =
        new OpcUaServer(
            configBuilder.build(),
            transportProfile -> {
              if (transportProfile == TransportProfile.TCP_UASC_UABINARY) {
                return new OpcTcpServerTransport(OpcTcpServerTransportConfig.newBuilder().build());
              }
              throw new RuntimeException("unexpected TransportProfile: " + transportProfile);
            });

    server.startup().get(10, TimeUnit.SECONDS);

    return new SksTestServer(
        server, trustListManager, "opc.tcp://localhost:%d/sks-test".formatted(port));
  }

  OpcUaServer getServer() {
    return server;
  }

  /** The opc.tcp endpoint URL clients discover against. */
  String getEndpointUrl() {
    return endpointUrl;
  }

  /** Trust {@code certificate} so a client presenting it can open a secured channel. */
  void trustClientCertificate(X509Certificate certificate) {
    trustListManager.addTrustedCertificate(certificate);
  }

  @Override
  public void close() {
    try {
      server.shutdown().get(10, TimeUnit.SECONDS);
    } catch (Exception e) {
      // best-effort shutdown; test failures are reported by the tests themselves
    }
  }

  private static Set<EndpointConfig> endpointConfigs(X509Certificate certificate, int port) {
    EndpointConfig.Builder base =
        EndpointConfig.newBuilder()
            .setBindAddress("localhost")
            .setHostname("localhost")
            .setPath("/sks-test")
            .setCertificate(certificate)
            .setTransportProfile(TransportProfile.TCP_UASC_UABINARY)
            .setBindPort(port)
            .addTokenPolicies(USER_TOKEN_POLICY_ANONYMOUS, USER_TOKEN_POLICY_USERNAME);

    var endpoints = new LinkedHashSet<EndpointConfig>();

    endpoints.add(
        base.copy()
            .setSecurityPolicy(SecurityPolicy.None)
            .setSecurityMode(MessageSecurityMode.None)
            .build());
    endpoints.add(
        base.copy()
            .setSecurityPolicy(SecurityPolicy.Basic256Sha256)
            .setSecurityMode(MessageSecurityMode.Sign)
            .build());
    endpoints.add(
        base.copy()
            .setSecurityPolicy(SecurityPolicy.Basic256Sha256)
            .setSecurityMode(MessageSecurityMode.SignAndEncrypt)
            .build());

    return endpoints;
  }

  /** Bind-and-close probe for a currently free TCP port; the small race is accepted. */
  private static int freeTcpPort() throws Exception {
    try (ServerSocket socket = new ServerSocket(0)) {
      return socket.getLocalPort();
    }
  }
}
