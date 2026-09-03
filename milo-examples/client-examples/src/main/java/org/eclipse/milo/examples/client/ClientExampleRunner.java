/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.examples.client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Security;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.eclipse.milo.examples.server.ExampleServer;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.security.CertificateManager;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateGroup;
import org.eclipse.milo.opcua.stack.core.security.DefaultClientCertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.FileBasedTrustListManager;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientExampleRunner {

  static {
    // Required for SecurityPolicy.Aes256_Sha256_RsaPss
    Security.addProvider(new BouncyCastleProvider());
  }

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final CompletableFuture<OpcUaClient> future = new CompletableFuture<>();

  private ExampleServer exampleServer;

  private final ClientExample clientExample;
  private final boolean serverRequired;
  private final Path securityTempDir;
  private final FileBasedTrustListManager clientTrustListManager;

  public ClientExampleRunner(ClientExample clientExample) throws Exception {
    this(clientExample, true);
  }

  public ClientExampleRunner(ClientExample clientExample, boolean serverRequired) throws Exception {
    this.clientExample = clientExample;
    this.serverRequired = serverRequired;

    if (serverRequired) {
      int port = EndpointUtil.getPort(clientExample.getEndpointUrl());
      exampleServer = new ExampleServer(port, clientExample::configureServer);
      exampleServer.startup().get();
    }

    securityTempDir = Paths.get(System.getProperty("java.io.tmpdir"), "client", "security");
    Files.createDirectories(securityTempDir);
    if (!Files.exists(securityTempDir)) {
      throw new Exception("unable to create security dir: " + securityTempDir);
    }

    Path pkiDir = securityTempDir.resolve("pki");

    LoggerFactory.getLogger(getClass()).info("security dir: {}", securityTempDir.toAbsolutePath());
    LoggerFactory.getLogger(getClass()).info("security pki dir: {}", pkiDir.toAbsolutePath());

    clientTrustListManager = FileBasedTrustListManager.createAndInitialize(pkiDir);
  }

  private OpcUaClient createClient() throws Exception {
    KeyStoreLoader loader = new KeyStoreLoader().load(securityTempDir);

    var certificateQuarantine = new MemoryCertificateQuarantine();
    var certificateValidator =
        new DefaultClientCertificateValidator(clientTrustListManager, certificateQuarantine);

    // The example client has one key pair and certificate chain on hand. forIdentity wraps them in
    // a group of one that shares the file-based trust list with the validator.
    var certificateGroup =
        DefaultCertificateGroup.forIdentity(
            loader.getClientKeyPair(),
            loader.getClientCertificateChain(),
            clientTrustListManager,
            certificateQuarantine,
            certificateValidator);

    return OpcUaClient.create(
        clientExample.getEndpointUrl(),
        endpoints -> endpoints.stream().filter(clientExample.endpointFilter()).findFirst(),
        transportConfigBuilder -> {},
        clientConfigBuilder -> {
          clientConfigBuilder
              .setApplicationName(LocalizedText.english("eclipse milo opc-ua client"))
              .setApplicationUri("urn:eclipse:milo:examples:client")
              .setCertificateGroup(certificateGroup)
              .setCertificateValidator(certificateValidator)
              .setIdentityProvider(clientExample.getIdentityProvider());
          clientExample.configureClient(clientConfigBuilder);
        });
  }

  public void run() {
    try {
      OpcUaClient client = createClient();

      // For the sake of the examples we will create mutual trust between the client and
      // server, so we can run them with security enabled by default.
      // If the client example is pointed at another server then the rejected certificate
      // will need to be moved from the security "pki/rejected" directory to the
      // "pki/trusted/certs" directory.

      if (serverRequired && exampleServer != null) {
        CertificateManager certificateManager =
            exampleServer.getServer().getConfig().getCertificateManager();

        // Make the example server trust the example client certificate by default.
        client
            .getConfig()
            .getCertificateGroup()
            .ifPresent(
                clientGroup ->
                    clientGroup
                        .getCertificateIdentities()
                        .forEach(
                            identity ->
                                certificateManager
                                    .getCertificateGroups()
                                    .forEach(
                                        group ->
                                            group
                                                .getTrustListManager()
                                                .addTrustedCertificate(identity.certificate()))));

        // Make the example client trust the example server certificate by default.
        exampleServer
            .getServer()
            .getConfig()
            .getCertificateManager()
            .getCertificateGroups()
            .forEach(
                certificateGroup ->
                    certificateGroup
                        .getCertificateEntries()
                        .forEach(
                            entry ->
                                clientTrustListManager.addTrustedCertificate(
                                    entry.certificateChain()[0])));
      }

      future.whenCompleteAsync(
          (c, ex) -> {
            if (ex != null) {
              logger.error("Error running example: {}", ex.getMessage(), ex);
            }

            try {
              client.disconnectAsync().get();
              if (serverRequired && exampleServer != null) {
                exampleServer.shutdown().get();
              }
            } catch (ExecutionException | InterruptedException e) {
              logger.error("Error disconnecting: {}", e.getMessage(), e);
            } finally {
              closeClientTrustListManager();
              Stack.releaseSharedResources();
            }

            try {
              Thread.sleep(1000);
              System.exit(0);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          });

      try {
        clientExample.run(client, future);
        future.get(15, TimeUnit.SECONDS);
      } catch (Throwable t) {
        logger.error("Error running client example: {}", t.getMessage(), t);
        future.completeExceptionally(t);
      }
    } catch (Throwable t) {
      logger.error("Error getting client: {}", t.getMessage(), t);

      future.completeExceptionally(t);
      closeClientTrustListManager();

      try {
        Thread.sleep(1000);
        System.exit(0);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    try {
      Thread.sleep(999_999_999);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private void closeClientTrustListManager() {
    try {
      clientTrustListManager.close();
    } catch (IOException e) {
      logger.error("Error closing TrustListManager: {}", e.getMessage(), e);
    }
  }
}
