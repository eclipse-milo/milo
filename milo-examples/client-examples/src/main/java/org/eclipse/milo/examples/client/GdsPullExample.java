/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.examples.client;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.gds.GdsClient;
import org.eclipse.milo.opcua.sdk.client.gds.GdsClient.FinishRequestResult;
import org.eclipse.milo.opcua.sdk.client.gds.GdsClient.TrustListInfo;
import org.eclipse.milo.opcua.sdk.client.gds.TrustListApplier;
import org.eclipse.milo.opcua.sdk.client.gds.TrustListReader;
import org.eclipse.milo.opcua.sdk.client.identity.IdentityProvider;
import org.eclipse.milo.opcua.sdk.client.identity.UsernameProvider;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.gds.types.ApplicationRecordDataType;
import org.eclipse.milo.opcua.stack.core.security.MemoryTrustListManager;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.structured.TrustListDataType;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Runs the GDS Pull Model (OPC 10000-12 §7.6) once against a Global Discovery Server: find or
 * register this client's application record, request a certificate for the default group, poll
 * until it is issued, then read the group's TrustList.
 *
 * <p>Point it at a GDS with the {@code gds.endpoint}, {@code gds.username}, and {@code
 * gds.password} system properties; the defaults match the OPC Foundation reference GDS from
 * UA-.NETStandard. Registration and signing need an administrator account on most servers, and the
 * GDS must trust the example client certificate (move it from its rejected store after the first
 * attempt). The issued certificate and trust list are only printed; nothing is installed.
 */
public class GdsPullExample implements ClientExample {

  public static void main(String[] args) throws Exception {
    GdsPullExample example = new GdsPullExample();

    new ClientExampleRunner(example, false).run();
  }

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public String getEndpointUrl() {
    return System.getProperty("gds.endpoint", "opc.tcp://localhost:58810/GlobalDiscoveryServer");
  }

  @Override
  public IdentityProvider getIdentityProvider() {
    return new UsernameProvider(
        System.getProperty("gds.username", "appadmin"), System.getProperty("gds.password", "demo"));
  }

  @Override
  public void run(OpcUaClient client, CompletableFuture<OpcUaClient> future) throws Exception {
    client.connect();

    GdsClient gds = GdsClient.create(client);
    String applicationUri = client.getConfig().getApplicationUri().orElseThrow();

    // Part 12 §6.4: look for an existing registration before creating one.
    ApplicationRecordDataType[] found = gds.findApplications(applicationUri);
    NodeId applicationId;
    if (found.length == 0) {
      applicationId = gds.registerApplication(clientRecord(client));
      logger.info("Registered {} as {}", applicationUri, applicationId);
    } else {
      applicationId = found[0].getApplicationId();
      logger.info("Found {} registration(s); using {}", found.length, applicationId);
    }

    NodeId groupId = gds.getCertificateGroups(applicationId)[0];
    // Null means "the group's default", which resolve selects when the GDS advertises only the
    // abstract ApplicationCertificateType.
    NodeId certificateTypeId =
        gds.resolveCertificateTypeId(groupId, NodeIds.RsaSha256ApplicationCertificateType);
    logger.info(
        "Group {} resolved request type {}; update required: {}",
        groupId,
        certificateTypeId,
        gds.getCertificateStatus(applicationId, groupId, certificateTypeId));

    // A throwaway key pair; a real application would use the key pair of the certificate it is
    // renewing and install the result through its CertificateManager.
    KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    ByteString csr =
        ByteString.of(
            CertificateUtil.generateCsr(
                    keyPair,
                    "CN=Eclipse Milo GDS Pull Example",
                    applicationUri,
                    List.of("localhost"),
                    List.of(),
                    "SHA256withRSA")
                .getEncoded());

    NodeId requestId = gds.startSigningRequest(applicationId, groupId, certificateTypeId, csr);
    logger.info("Signing request {} submitted", requestId);

    FinishRequestResult issued = awaitIssued(gds, applicationId, requestId);
    X509Certificate certificate =
        CertificateUtil.decodeCertificate(issued.certificate().bytesOrEmpty());
    GdsClient.verifyIssuedCertificate(certificate, keyPair.getPublic(), applicationUri);
    logger.info(
        "Issued certificate subject={} issuer={} notAfter={}",
        certificate.getSubjectX500Principal(),
        certificate.getIssuerX500Principal(),
        certificate.getNotAfter());

    NodeId trustListId = gds.getTrustList(applicationId, groupId);
    TrustListInfo info = gds.readTrustListInfo(trustListId);
    TrustListDataType trustList = TrustListReader.read(client, trustListId);

    var trustListManager = new MemoryTrustListManager();
    TrustListApplier.apply(trustList, trustListManager);
    logger.info(
        "TrustList {} (lastUpdateTime={}, updateFrequency={}): {} trusted certificates, {} trusted"
            + " CRLs, {} issuer certificates, {} issuer CRLs",
        trustListId,
        info.lastUpdateTime(),
        info.updateFrequency(),
        trustListManager.getTrustedCertificates().size(),
        trustListManager.getTrustedCrls().size(),
        trustListManager.getIssuerCertificates().size(),
        trustListManager.getIssuerCrls().size());

    future.complete(client);
  }

  /** Poll FinishRequest while the GDS answers Bad_NothingToDo, i.e. while approval is pending. */
  private FinishRequestResult awaitIssued(GdsClient gds, NodeId applicationId, NodeId requestId)
      throws Exception {

    for (int attempt = 1; ; attempt++) {
      try {
        return gds.finishRequest(applicationId, requestId);
      } catch (UaException e) {
        if (e.getStatusCode().value() != StatusCodes.Bad_NothingToDo || attempt >= 30) {
          throw e;
        }
        logger.info("Request {} pending (attempt {}); approve it in the GDS", requestId, attempt);
        TimeUnit.SECONDS.sleep(2);
      }
    }
  }

  private static ApplicationRecordDataType clientRecord(OpcUaClient client) {
    return new ApplicationRecordDataType(
        NodeId.NULL_VALUE,
        client.getConfig().getApplicationUri().orElseThrow(),
        ApplicationType.Client,
        new LocalizedText[] {client.getConfig().getApplicationName()},
        client.getConfig().getProductUri(),
        null,
        null);
  }
}
