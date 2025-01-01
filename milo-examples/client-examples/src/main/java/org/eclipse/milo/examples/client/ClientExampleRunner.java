/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.examples.client;

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
import org.eclipse.milo.opcua.stack.core.security.DefaultClientCertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.FileBasedTrustListManager;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.TrustListManager;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
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

  private TrustListManager clientTrustListManager;

  private final ClientExample clientExample;
  private final boolean serverRequired;

  public ClientExampleRunner(ClientExample clientExample) throws Exception {
    this(clientExample, true);
  }

  public ClientExampleRunner(ClientExample clientExample, boolean serverRequired) throws Exception {
    this.clientExample = clientExample;
    this.serverRequired = serverRequired;

    if (serverRequired) {
      exampleServer = new ExampleServer();
      exampleServer.startup().get();
    }
  }

  private OpcUaClient createClient() throws Exception {
    Path securityTempDir = Paths.get(System.getProperty("java.io.tmpdir"), "client", "security");
    Files.createDirectories(securityTempDir);
    if (!Files.exists(securityTempDir)) {
      throw new Exception("unable to create security dir: " + securityTempDir);
    }

    Path pkiDir = securityTempDir.resolve("pki");

    LoggerFactory.getLogger(getClass()).info("security dir: {}", securityTempDir.toAbsolutePath());
    LoggerFactory.getLogger(getClass()).info("security pki dir: {}", pkiDir.toAbsolutePath());

    KeyStoreLoader loader = new KeyStoreLoader().load(securityTempDir);

    clientTrustListManager = FileBasedTrustListManager.createAndInitialize(pkiDir);

    var certificateValidator =
        new DefaultClientCertificateValidator(
            clientTrustListManager, new MemoryCertificateQuarantine());

    return OpcUaClient.create(
        clientExample.getEndpointUrl(),
        endpoints -> endpoints.stream().filter(clientExample.endpointFilter()).findFirst(),
        transportConfigBuilder -> {},
        clientConfigBuilder ->
            clientConfigBuilder
                .setApplicationName(LocalizedText.english("eclipse milo opc-ua client"))
                .setApplicationUri("urn:eclipse:milo:examples:client")
                .setKeyPair(loader.getClientKeyPair())
                .setCertificate(loader.getClientCertificate())
                .setCertificateChain(loader.getClientCertificateChain())
                .setCertificateValidator(certificateValidator)
                .setIdentityProvider(clientExample.getIdentityProvider()));
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
            .getCertificate()
            .ifPresent(
                certificate ->
                    certificateManager
                        .getCertificateGroups()
                        .forEach(
                            group ->
                                group.getTrustListManager().addTrustedCertificate(certificate)));

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
                                    entry.certificateChain[0])));
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
              Stack.releaseSharedResources();
            } catch (InterruptedException | ExecutionException e) {
              logger.error("Error disconnecting: {}", e.getMessage(), e);
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
}
