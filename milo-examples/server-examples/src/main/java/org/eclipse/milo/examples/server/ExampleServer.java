/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.examples.server;

import static org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig.USER_TOKEN_POLICY_ANONYMOUS;
import static org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig.USER_TOKEN_POLICY_USERNAME;
import static org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig.USER_TOKEN_POLICY_X509;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.identity.AnonymousIdentityValidator;
import org.eclipse.milo.opcua.sdk.server.identity.CompositeValidator;
import org.eclipse.milo.opcua.sdk.server.identity.UsernameIdentityValidator;
import org.eclipse.milo.opcua.sdk.server.identity.X509IdentityValidator;
import org.eclipse.milo.opcua.sdk.server.util.HostnameUtil;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.security.DefaultApplicationGroup;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateManager;
import org.eclipse.milo.opcua.stack.core.security.DefaultServerCertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.FileBasedCertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.FileBasedTrustListManager;
import org.eclipse.milo.opcua.stack.core.security.KeyStoreCertificateStore;
import org.eclipse.milo.opcua.stack.core.security.RsaSha256CertificateFactory;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.BuildInfo;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
import org.eclipse.milo.opcua.stack.core.util.NonceUtil;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransport;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransportConfig;
import org.slf4j.LoggerFactory;

public class ExampleServer {

  private static final int TCP_BIND_PORT = 12686;

  static {
    // Required for SecurityPolicy.Aes256_Sha256_RsaPss
    Security.addProvider(new BouncyCastleProvider());

    try {
      NonceUtil.blockUntilSecureRandomSeeded(10, TimeUnit.SECONDS);
    } catch (ExecutionException | InterruptedException | TimeoutException e) {
      e.printStackTrace();
      System.exit(-1);
    }
  }

  public static void main(String[] args) throws Exception {
    ExampleServer server = new ExampleServer();

    server.startup().get();

    final CompletableFuture<Void> future = new CompletableFuture<>();

    Runtime.getRuntime().addShutdownHook(new Thread(() -> future.complete(null)));

    future.get();
  }

  private final OpcUaServer server;
  private final ExampleNamespace exampleNamespace;

  public ExampleServer() throws Exception {
    Path securityTempDir = Paths.get(System.getProperty("java.io.tmpdir"), "server", "security");
    Files.createDirectories(securityTempDir);
    if (!Files.exists(securityTempDir)) {
      throw new Exception("unable to create security temp dir: " + securityTempDir);
    }

    File pkiDir = securityTempDir.resolve("pki").toFile();

    LoggerFactory.getLogger(getClass()).info("security dir: {}", securityTempDir.toAbsolutePath());
    LoggerFactory.getLogger(getClass()).info("security pki dir: {}", pkiDir.getAbsolutePath());

    KeyStoreLoader loader = new KeyStoreLoader().load(securityTempDir);

    var certificateStore =
        KeyStoreCertificateStore.createAndInitialize(
            new KeyStoreCertificateStore.Settings(
                securityTempDir.resolve("example-server.pfx"),
                "password"::toCharArray,
                alias -> "password".toCharArray()));

    var trustListManager = FileBasedTrustListManager.createAndInitialize(pkiDir.toPath());

    var certificateQuarantine =
        FileBasedCertificateQuarantine.create(pkiDir.toPath().resolve("rejected").resolve("certs"));

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

    var identityValidator =
        new UsernameIdentityValidator(
            authChallenge -> {
              String username = authChallenge.getUsername();
              String password = authChallenge.getPassword();

              boolean userOk = "user".equals(username) && "password1".equals(password);
              boolean adminOk = "admin".equals(username) && "password2".equals(password);

              return userOk || adminOk;
            });

    var x509IdentityValidator = new X509IdentityValidator(c -> true);

    X509Certificate certificate = loader.getServerCertificate();

    // The configured application URI must match the one in the certificate(s)
    String applicationUri =
        CertificateUtil.getSanUri(certificate)
            .orElseThrow(
                () ->
                    new UaRuntimeException(
                        StatusCodes.Bad_ConfigurationError,
                        "certificate is missing the application URI"));

    Set<EndpointConfig> endpointConfigurations = createEndpointConfigs(certificate);

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
            .setIdentityValidator(
                new CompositeValidator(
                    AnonymousIdentityValidator.INSTANCE, identityValidator, x509IdentityValidator))
            .setProductUri("urn:eclipse:milo:example-server")
            .build();

    server =
        new OpcUaServer(
            serverConfig,
            transportProfile -> {
              assert transportProfile == TransportProfile.TCP_UASC_UABINARY;

              OpcTcpServerTransportConfig transportConfig =
                  OpcTcpServerTransportConfig.newBuilder().build();

              return new OpcTcpServerTransport(transportConfig);
            });

    exampleNamespace = new ExampleNamespace(server);
    exampleNamespace.startup();
  }

  private Set<EndpointConfig> createEndpointConfigs(X509Certificate certificate) {
    var endpointConfigs = new LinkedHashSet<EndpointConfig>();

    List<String> bindAddresses = List.of("0.0.0.0");

    var hostnames = new LinkedHashSet<String>();
    hostnames.add(HostnameUtil.getHostname());
    hostnames.addAll(HostnameUtil.getHostnames("0.0.0.0", true, false));

    for (String bindAddress : bindAddresses) {
      for (String hostname : hostnames) {
        EndpointConfig.Builder builder =
            EndpointConfig.newBuilder()
                .setBindAddress(bindAddress)
                .setHostname(hostname)
                .setPath("/milo")
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

        endpointConfigs.add(buildTcpEndpoint(noSecurityBuilder));

        // TCP Basic256Sha256 / SignAndEncrypt
        endpointConfigs.add(
            buildTcpEndpoint(
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
                .setPath("/milo/discovery")
                .setSecurityPolicy(SecurityPolicy.None)
                .setSecurityMode(MessageSecurityMode.None);

        endpointConfigs.add(buildTcpEndpoint(discoveryBuilder));
      }
    }

    return endpointConfigs;
  }

  private static EndpointConfig buildTcpEndpoint(EndpointConfig.Builder base) {
    return base.copy()
        .setTransportProfile(TransportProfile.TCP_UASC_UABINARY)
        .setBindPort(TCP_BIND_PORT)
        .build();
  }

  public OpcUaServer getServer() {
    return server;
  }

  public CompletableFuture<OpcUaServer> startup() {
    return server.startup();
  }

  public CompletableFuture<OpcUaServer> shutdown() {
    exampleNamespace.shutdown();

    return server.shutdown();
  }
}
