/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.test;

import static org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig.USER_TOKEN_POLICY_ANONYMOUS;
import static org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig.USER_TOKEN_POLICY_USERNAME;
import static org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig.USER_TOKEN_POLICY_X509;

import java.io.File;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.security.KeyPair;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.identity.UsernameIdentityValidator;
import org.eclipse.milo.opcua.sdk.server.util.HostnameUtil;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
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
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedHttpsCertificateBuilder;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransport;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransportConfig;
import org.slf4j.LoggerFactory;

public final class TestServer {

  private static final int TCP_BIND_PORT = 12686;
  private static final int HTTPS_BIND_PORT = 8443;

  static {
    // Required for SecurityPolicy.Aes256_Sha256_RsaPss
    Security.addProvider(new BouncyCastleProvider());
  }

  private TestServer() {}

  public static OpcUaServer create() throws Exception {
    int port = new Random().nextInt(65535 - 10000) + 10000;

    try {
      ServerSocket ss = new ServerSocket();
      InetSocketAddress isa = new InetSocketAddress(InetAddress.getLocalHost(), port);
      ss.bind(isa);
      ss.close();

      return create(port);
    } catch (Throwable t) {
      return create();
    }
  }

  public static OpcUaServer create(int port) throws Exception {
    File securityTempDir = new File(System.getProperty("java.io.tmpdir"), "security");
    if (!securityTempDir.exists() && !securityTempDir.mkdirs()) {
      throw new Exception("unable to create security temp dir: " + securityTempDir);
    }
    LoggerFactory.getLogger(TestServer.class)
        .info("security temp dir: {}", securityTempDir.getAbsolutePath());

    File pkiDir = securityTempDir.toPath().resolve("pki").toFile();
    LoggerFactory.getLogger(TestServer.class).info("pki dir: {}", pkiDir.getAbsolutePath());

    KeyStoreLoader loader = new KeyStoreLoader().load(securityTempDir);

    var trustListManager = new MemoryTrustListManager();
    var certificateStore = new MemoryCertificateStore();
    var certificateQuarantine = new MemoryCertificateQuarantine();

    var certificateFactory =
        new RsaSha256CertificateFactory() {
          @Override
          protected KeyPair createRsaSha256KeyPair() {
            return loader.getServerKeyPair();
          }

          @Override
          protected X509Certificate[] createRsaSha256CertificateChain(KeyPair keyPair) {
            return loader.getServerCertificateChain();
          }
        };

    var certificateValidator =
        new DefaultServerCertificateValidator(trustListManager, certificateQuarantine);

    var defaultGroup =
        DefaultApplicationGroup.createAndInitialize(
            trustListManager, certificateStore, certificateFactory, certificateValidator);

    var certificateManager = new DefaultCertificateManager(certificateQuarantine, defaultGroup);

    KeyPair httpsKeyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);

    SelfSignedHttpsCertificateBuilder httpsCertificateBuilder =
        new SelfSignedHttpsCertificateBuilder(httpsKeyPair);
    httpsCertificateBuilder.setCommonName(HostnameUtil.getHostname());
    HostnameUtil.getHostnames("localhost", false).forEach(httpsCertificateBuilder::addDnsName);
    X509Certificate httpsCertificate = httpsCertificateBuilder.build();

    UsernameIdentityValidator identityValidator =
        new UsernameIdentityValidator(
            true,
            authChallenge -> {
              String username = authChallenge.getUsername();
              String password = authChallenge.getPassword();

              boolean user1 = "user1".equals(username) && "password".equals(password);
              boolean user2 = "user2".equals(username) && "password".equals(password);
              boolean admin = "admin".equals(username) && "password".equals(password);

              return user1 || user2 || admin;
            });

    X509Certificate certificate = loader.getServerCertificate();

    // The configured application URI must match the one in the certificate(s)
    String applicationUri =
        CertificateUtil.getSanUri(certificate)
            .orElseThrow(
                () ->
                    new UaRuntimeException(
                        StatusCodes.Bad_ConfigurationError,
                        "certificate is missing the application URI"));

    Set<EndpointConfig> endpointConfigurations = createEndpointConfigs(certificate, port);

    OpcUaServerConfig serverConfig =
        OpcUaServerConfig.builder()
            .setApplicationUri(applicationUri)
            .setApplicationName(LocalizedText.english("Eclipse Milo OPC UA Example Server"))
            .setEndpoints(endpointConfigurations)
            .setBuildInfo(
                new BuildInfo(
                    "urn:eclipse:milo:example-server",
                    "eclipse",
                    "eclipse milo example server",
                    OpcUaServer.SDK_VERSION,
                    "",
                    DateTime.now()))
            .setCertificateManager(certificateManager)
            .setIdentityValidator(identityValidator)
            .setProductUri("urn:eclipse:milo:example-server")
            .build();

    return new OpcUaServer(
        serverConfig,
        transportProfile -> {
          if (transportProfile == TransportProfile.TCP_UASC_UABINARY) {
            OpcTcpServerTransportConfig transportConfig =
                OpcTcpServerTransportConfig.newBuilder().build();

            return new OpcTcpServerTransport(transportConfig);
          } else {
            throw new RuntimeException("unexpected TransportProfile: " + transportProfile);
          }
        });
  }

  private static Set<EndpointConfig> createEndpointConfigs(X509Certificate certificate, int port) {
    Set<EndpointConfig> endpointConfigurations = new LinkedHashSet<>();

    var bindAddresses = new ArrayList<String>();
    bindAddresses.add("localhost");

    Set<String> hostnames = HostnameUtil.getHostnames("localhost", true);

    for (String bindAddress : bindAddresses) {
      for (String hostname : hostnames) {
        EndpointConfig.Builder builder =
            EndpointConfig.newBuilder()
                .setBindAddress(bindAddress)
                .setHostname(hostname)
                .setPath("/test")
                .setCertificate(certificate)
                .addTokenPolicies(
                    USER_TOKEN_POLICY_ANONYMOUS,
                    USER_TOKEN_POLICY_USERNAME,
                    USER_TOKEN_POLICY_X509);

        EndpointConfig.Builder noSecurityBuilder =
            builder
                .copy()
                .setSecurityPolicy(SecurityPolicy.None)
                .setSecurityMode(MessageSecurityMode.None);

        endpointConfigurations.add(buildTcpEndpoint(port, noSecurityBuilder));

        // TCP Basic256Sha256 / SignAndEncrypt
        endpointConfigurations.add(
            buildTcpEndpoint(
                port,
                builder
                    .copy()
                    .setSecurityPolicy(SecurityPolicy.Basic256Sha256)
                    .setSecurityMode(MessageSecurityMode.SignAndEncrypt)));

        /*
         * It's good practice to provide a discovery-specific endpoint with no security.
         * It's required practice if all regular endpoints have security configured.
         *
         * Usage of the  "/discovery" suffix is defined by OPC UA Part 6:
         *
         * Each OPC UA Server Application implements the Discovery Service Set. If the OPC UA Server requires a
         * different address for this Endpoint it shall create the address by appending the path "/discovery" to
         * its base address.
         */

        EndpointConfig.Builder discoveryBuilder =
            builder
                .copy()
                .setPath("/test/discovery")
                .setSecurityPolicy(SecurityPolicy.None)
                .setSecurityMode(MessageSecurityMode.None);

        endpointConfigurations.add(buildTcpEndpoint(port, discoveryBuilder));
      }
    }

    return endpointConfigurations;
  }

  private static EndpointConfig buildTcpEndpoint(int port, EndpointConfig.Builder base) {
    return base.copy()
        .setTransportProfile(TransportProfile.TCP_UASC_UABINARY)
        .setBindPort(port)
        .build();
  }
}
