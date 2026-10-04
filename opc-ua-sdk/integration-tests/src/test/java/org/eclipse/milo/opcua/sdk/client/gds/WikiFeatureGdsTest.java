/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.gds;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.gds.testing.FakeGdsNamespace.MethodAccess;
import org.eclipse.milo.opcua.sdk.client.methods.UaMethodException;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.gds.types.ApplicationRecordDataType;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateGroup;
import org.eclipse.milo.opcua.stack.core.security.DefaultClientCertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateStore;
import org.eclipse.milo.opcua.stack.core.security.MemoryTrustListManager;
import org.eclipse.milo.opcua.stack.core.security.TrustListManager;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TrustListMasks;
import org.eclipse.milo.opcua.stack.core.types.structured.TrustListDataType;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

@Timeout(30)
class WikiFeatureGdsTest extends AbstractGdsClientTest {
  // Registration must reuse an existing application rather than register on every restart.
  @Test
  void findOrRegisterReusesTheRecordAndPreservesAuthorizationErrors() throws Exception {
    NodeId first = findOrRegister(client, clientRecord());
    NodeId second = findOrRegister(client, clientRecord());
    assertEquals(first, second);
    assertEquals(1, gds.getRegisterApplicationCallCount());
    gds.reset();
    gds.setRegisterApplicationAccess(MethodAccess.NOBODY);
    UaException denied =
        assertThrows(UaException.class, () -> findOrRegister(client, clientRecord()));
    assertEquals(StatusCodes.Bad_UserAccessDenied, denied.getStatusCode().value());
  }

  // Trust replacement and malformed-file cleanup are observed over a real FileType service.
  @Test
  void pullReplacesSelectedTrustListsAndMalformedInputLeavesThemUnchanged() throws Exception {
    NodeId applicationId = findOrRegister(client, clientRecord());
    var file = gds.getApplicationGroupTrustList();
    file.setTrustList(
        new TrustListDataType(
            uint(TrustListMasks.TrustedCertificates.getValue()),
            new ByteString[] {ByteString.of(testServer.getClientCertificate().getEncoded())},
            new ByteString[0],
            new ByteString[0],
            new ByteString[0]));
    var trust = new MemoryTrustListManager();
    trust.addTrustedCertificate(gds.getCaCertificate());
    trust.addIssuerCertificate(gds.getCaCertificate());
    pullTrustList(client, applicationId, gds.defaultApplicationGroupId(), trust);
    assertEquals(1, trust.getSnapshot().trustedCertificates().size());
    assertEquals(
        testServer.getClientCertificate(), trust.getSnapshot().trustedCertificates().get(0));
    assertEquals(
        java.util.List.of(gds.getCaCertificate()), trust.getSnapshot().issuerCertificates());
    assertEquals(0, file.openHandles());
    file.setBody(new byte[] {1, 2, 3});
    UaException malformed =
        assertThrows(
            UaException.class,
            () -> pullTrustList(client, applicationId, gds.defaultApplicationGroupId(), trust));
    assertEquals(StatusCodes.Bad_DecodingError, malformed.getStatusCode().value());
    assertEquals(
        java.util.List.of(testServer.getClientCertificate()),
        trust.getSnapshot().trustedCertificates());
    assertEquals(
        java.util.List.of(gds.getCaCertificate()), trust.getSnapshot().issuerCertificates());
    assertEquals(0, file.openHandles());
  }

  // Issuance is incomplete until the requested identity has actually reached the local group.
  @Test
  void pendingSigningRequestInstallsTheIssuedIdentityWithoutReplacingItsKey() throws Exception {
    gds.setPollsBeforeIssued(1);
    NodeId applicationId = findOrRegister(client, clientRecord());
    KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    var trust = new MemoryTrustListManager();
    var quarantine = new MemoryCertificateQuarantine();
    var group =
        new DefaultCertificateGroup(
            trust,
            new MemoryCertificateStore(),
            quarantine,
            new DefaultClientCertificateValidator(trust, quarantine));
    X509Certificate certificate =
        requestAndInstall(
            gdsClient,
            applicationId,
            gds.defaultApplicationGroupId(),
            keyPair,
            APPLICATION_URI,
            csr(keyPair, APPLICATION_URI),
            gds.getCaCertificate(),
            group);
    assertEquals(1, group.getCertificateIdentities().size());
    assertEquals(certificate, group.getCertificateIdentities().get(0).certificate());
    KeyPair installedKeyPair = group.getCertificateIdentities().get(0).keyPair();
    assertArrayEquals(keyPair.getPublic().getEncoded(), installedKeyPair.getPublic().getEncoded());
    assertArrayEquals(
        keyPair.getPrivate().getEncoded(), installedKeyPair.getPrivate().getEncoded());
    assertEquals(1, gds.getStartSigningRequestCallCount());
    assertEquals(2, gds.getFinishRequestCallCount());
  }

  // snippet:gds_signing:start
  static X509Certificate requestAndInstall(
      GdsClient gds,
      NodeId applicationId,
      NodeId groupId,
      KeyPair keyPair,
      String applicationUri,
      ByteString csr,
      X509Certificate issuingCa,
      DefaultCertificateGroup group)
      throws Exception {
    NodeId requestType =
        gds.resolveCertificateTypeId(groupId, NodeIds.RsaSha256ApplicationCertificateType);
    NodeId requestId = gds.startSigningRequest(applicationId, groupId, requestType, csr);
    GdsClient.FinishRequestResult issued;
    try {
      issued = gds.finishRequest(applicationId, requestId);
    } catch (UaMethodException pending) {
      if (pending.getStatusCode().value() != StatusCodes.Bad_NothingToDo) throw pending;
      // This bounded local example retries once. Production polling needs scheduling/persistence.
      issued = gds.finishRequest(applicationId, requestId);
    }
    X509Certificate certificate =
        CertificateUtil.decodeCertificate(issued.certificate().bytesOrEmpty());
    GdsClient.verifyIssuedCertificate(certificate, keyPair.getPublic(), applicationUri);
    certificate.checkValidity();
    certificate.verify(issuingCa.getPublicKey());
    group.updateCertificate(
        NodeIds.RsaSha256ApplicationCertificateType,
        keyPair,
        new X509Certificate[] {certificate, issuingCa});
    return certificate;
  }

  // snippet:gds_signing:end

  // snippet:gds_registration:start
  static NodeId findOrRegister(OpcUaClient client, ApplicationRecordDataType record)
      throws UaException {
    GdsClient gds = GdsClient.create(client);
    ApplicationRecordDataType[] found = gds.findApplications(record.getApplicationUri());
    if (found.length > 1) throw new IllegalStateException("Ambiguous application URI");
    return found.length == 0 ? gds.registerApplication(record) : found[0].getApplicationId();
  }

  // snippet:gds_registration:end

  // snippet:gds_trust:start
  static void pullTrustList(
      OpcUaClient client, NodeId applicationId, NodeId groupId, TrustListManager trust)
      throws UaException {
    GdsClient gds = GdsClient.create(client);
    NodeId trustListId = gds.getTrustList(applicationId, groupId);
    TrustListDataType update = TrustListReader.read(client, trustListId);
    TrustListApplier.apply(update, trust);
  }
  // snippet:gds_trust:end
}
