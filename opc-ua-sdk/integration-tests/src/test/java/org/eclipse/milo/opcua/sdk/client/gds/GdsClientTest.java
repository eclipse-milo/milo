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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.List;
import org.eclipse.milo.opcua.sdk.client.gds.GdsClient.CertificatesResult;
import org.eclipse.milo.opcua.sdk.client.gds.GdsClient.FinishRequestResult;
import org.eclipse.milo.opcua.sdk.client.gds.GdsClient.QueryApplicationsResult;
import org.eclipse.milo.opcua.sdk.client.gds.GdsClient.QueryServersResult;
import org.eclipse.milo.opcua.sdk.client.gds.GdsClient.RevocationStatus;
import org.eclipse.milo.opcua.sdk.client.gds.GdsClient.TrustListInfo;
import org.eclipse.milo.opcua.sdk.client.gds.model.objects.CertificateDirectoryTypeNode;
import org.eclipse.milo.opcua.sdk.client.methods.UaMethodException;
import org.eclipse.milo.opcua.sdk.client.nodes.UaObjectNode;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.gds.GdsNodeIds;
import org.eclipse.milo.opcua.stack.core.gds.types.ApplicationRecordDataType;
import org.eclipse.milo.opcua.stack.core.security.MemoryTrustListManager;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TrustListMasks;
import org.eclipse.milo.opcua.stack.core.types.structured.TrustListDataType;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class GdsClientTest extends AbstractGdsClientTest {

  @Nested
  class Creation {

    @Test
    void createResolvesTheNamespaceIndexAndDirectoryFromTheServer() {
      assertEquals(gds.nodeId(GdsNodeIds.Directory), gdsClient.getDirectoryId());
      assertEquals(gds.getNamespaceIndex(), gdsClient.getDirectoryId().getNamespaceIndex());
    }

    // The generated client model is only useful if create() registered it: the Directory must come
    // back as the typed node class and its navigation must resolve the GDS-namespace browse names.
    @Test
    void createRegistersGdsObjectTypesSoDirectoryResolvesToCertificateDirectoryTypeNode()
        throws Exception {

      UaObjectNode directory = client.getAddressSpace().getObjectNode(gdsClient.getDirectoryId());

      CertificateDirectoryTypeNode typed =
          assertInstanceOf(CertificateDirectoryTypeNode.class, directory);
      assertEquals(
          gds.nodeId(GdsNodeIds.Directory_CertificateGroups),
          typed.getCertificateGroupsNode().getNodeId());
    }
  }

  @Nested
  class Registration {

    @Test
    void findApplicationsReturnsEmptyForAnUnknownUri() throws Exception {
      ApplicationRecordDataType[] found = gdsClient.findApplications("urn:nobody");

      assertEquals(0, found.length);
    }

    // Part 12 §6.4: an application registers once and then finds itself by ApplicationUri; the
    // record round-trips through the GDS-namespace codec in both directions.
    @Test
    void registerThenFindReturnsTheRecordWithTheAssignedId() throws Exception {
      NodeId applicationId = registerTestApplication();

      ApplicationRecordDataType[] found = gdsClient.findApplications(APPLICATION_URI);

      assertEquals(1, found.length);
      assertEquals(applicationId, found[0].getApplicationId());
      assertEquals(APPLICATION_URI, found[0].getApplicationUri());
      assertEquals(ApplicationType.Client, found[0].getApplicationType());
      assertEquals(found[0], gdsClient.getApplication(applicationId));
    }

    @Test
    void registerApplicationWithoutAdminRoleFailsWithBadUserAccessDenied() {
      gds.setRegistrationAllowed(false);

      UaException e = assertThrows(UaException.class, GdsClientTest.this::registerTestApplication);

      assertEquals(StatusCodes.Bad_UserAccessDenied, e.getStatusCode().value());
      assertInstanceOf(UaMethodException.class, e, "carries the method result");
    }

    @Test
    void getApplicationOfUnknownIdFailsWithBadNotFound() {
      UaException e =
          assertThrows(UaException.class, () -> gdsClient.getApplication(newNodeId("nope")));

      assertEquals(StatusCodes.Bad_NotFound, e.getStatusCode().value());
    }

    @Test
    void updateApplicationReplacesTheStoredRecord() throws Exception {
      NodeId applicationId = registerTestApplication();
      var updated =
          new ApplicationRecordDataType(
              applicationId,
              APPLICATION_URI,
              ApplicationType.ClientAndServer,
              new LocalizedText[] {LocalizedText.english("Renamed")},
              "urn:eclipse:milo:test:product:2",
              new String[] {"opc.tcp://localhost:4840"},
              new String[] {"NA"});

      gdsClient.updateApplication(updated);

      assertEquals(updated, gdsClient.getApplication(applicationId));
    }

    @Test
    void unregisterApplicationRemovesTheRecord() throws Exception {
      NodeId applicationId = registerTestApplication();

      gdsClient.unregisterApplication(applicationId);

      assertEquals(0, gdsClient.findApplications(APPLICATION_URI).length);
    }

    @Test
    void queryApplicationsDecodesApplicationDescriptions() throws Exception {
      registerTestApplication();

      QueryApplicationsResult result =
          gdsClient.queryApplications(uint(0), uint(0), null, null, uint(0), null, null);

      assertEquals(1, result.applications().length);
      assertEquals(APPLICATION_URI, result.applications()[0].getApplicationUri());
      assertNotNull(result.lastCounterResetTime());
      assertEquals(uint(0), result.nextRecordId());
    }

    @Test
    void queryServersDecodesServerOnNetworkRecords() throws Exception {
      registerTestApplication();

      QueryServersResult result = gdsClient.queryServers(uint(0), uint(0), null, null, null, null);

      assertEquals(1, result.servers().length);
      assertEquals("Milo GDS Test Client", result.servers()[0].getServerName());
    }
  }

  @Nested
  class CertificateGroups {

    @Test
    void getCertificateGroupsReturnsTheGroupsAndReadCertificateTypesReadsEachGroupsProperty()
        throws Exception {

      NodeId applicationId = registerTestApplication();

      NodeId[] groups = gdsClient.getCertificateGroups(applicationId);

      assertArrayEquals(
          new NodeId[] {gds.defaultApplicationGroupId(), gds.defaultUserTokenGroupId()}, groups);
      assertArrayEquals(
          new NodeId[] {
            NodeIds.RsaSha256ApplicationCertificateType,
            NodeIds.EccNistP256ApplicationCertificateType
          },
          gdsClient.readCertificateTypes(groups[0]));
      assertArrayEquals(
          new NodeId[] {NodeIds.RsaSha256ApplicationCertificateType},
          gdsClient.readCertificateTypes(groups[1]));
    }

    @Test
    void readCertificateTypesOfAnObjectWithoutThePropertyFailsWithBadNotFound() {
      UaException e =
          assertThrows(
              UaException.class, () -> gdsClient.readCertificateTypes(gdsClient.getDirectoryId()));

      assertEquals(StatusCodes.Bad_NotFound, e.getStatusCode().value());
    }

    @Test
    void getCertificateStatusReportsWhetherAnUpdateIsRequired() throws Exception {
      NodeId applicationId = registerTestApplication();

      gds.setUpdateRequired(true);
      assertTrue(gdsClient.getCertificateStatus(applicationId, null, null));

      gds.setUpdateRequired(false);
      assertFalse(gdsClient.getCertificateStatus(applicationId, null, null));
    }

    @Test
    void getTrustListReturnsTheGroupsTrustListAndReadTrustListInfoReadsItsProperties()
        throws Exception {

      NodeId applicationId = registerTestApplication();

      NodeId trustListId = gdsClient.getTrustList(applicationId, gds.defaultApplicationGroupId());
      TrustListInfo info = gdsClient.readTrustListInfo(trustListId);

      assertEquals(gds.defaultApplicationGroupTrustListId(), trustListId);
      assertEquals(gds.getApplicationGroupTrustList().lastUpdateTime(), info.lastUpdateTime());
      assertEquals(60_000.0, info.updateFrequency());
    }

    // UpdateFrequency is optional on TrustListType; a GDS that omits it must not make the whole
    // read fail, the Pull client just has no server-suggested cycle period.
    @Test
    void readTrustListInfoReturnsNullUpdateFrequencyWhenTheGdsOmitsIt() throws Exception {
      NodeId applicationId = registerTestApplication();

      NodeId trustListId = gdsClient.getTrustList(applicationId, gds.defaultUserTokenGroupId());
      TrustListInfo info = gdsClient.readTrustListInfo(trustListId);

      assertEquals(gds.defaultUserTokenGroupTrustListId(), trustListId);
      assertNull(info.updateFrequency());
    }

    @Test
    void getTrustListOfUnknownGroupFailsWithBadNotFound() throws Exception {
      NodeId applicationId = registerTestApplication();

      UaException e =
          assertThrows(
              UaException.class, () -> gdsClient.getTrustList(applicationId, newNodeId("nope")));

      assertEquals(StatusCodes.Bad_NotFound, e.getStatusCode().value());
    }
  }

  @Nested
  class CertificateRequests {

    // Part 12 §7.9.3: the CSR's ApplicationUri must match the record, and the GDS's rejection code
    // must reach the caller unchanged so it can tell a bad request from a pending one.
    @Test
    void startSigningRequestWithMismatchedApplicationUriFailsWithBadCertificateUriInvalid()
        throws Exception {

      NodeId applicationId = registerTestApplication();
      KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);

      UaException e =
          assertThrows(
              UaException.class,
              () ->
                  gdsClient.startSigningRequest(
                      applicationId, null, null, csr(keyPair, "urn:someone:else")));

      assertEquals(StatusCodes.Bad_CertificateUriInvalid, e.getStatusCode().value());
    }

    // Part 12 §7.6: FinishRequest answers Bad_NothingToDo until the request is approved; the
    // caller polls on that code and must not treat it as a rejection.
    @Test
    void finishRequestFailsWithBadNothingToDoUntilTheCertificateIsIssued() throws Exception {
      gds.setPollsBeforeIssued(2);
      NodeId applicationId = registerTestApplication();
      KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);

      NodeId requestId =
          gdsClient.startSigningRequest(
              applicationId,
              gds.defaultApplicationGroupId(),
              NodeIds.RsaSha256ApplicationCertificateType,
              csr(keyPair, APPLICATION_URI));

      UaException first =
          assertThrows(UaException.class, () -> gdsClient.finishRequest(applicationId, requestId));
      UaException second =
          assertThrows(UaException.class, () -> gdsClient.finishRequest(applicationId, requestId));
      FinishRequestResult issued = gdsClient.finishRequest(applicationId, requestId);

      assertEquals(StatusCodes.Bad_NothingToDo, first.getStatusCode().value());
      assertEquals(StatusCodes.Bad_NothingToDo, second.getStatusCode().value());

      X509Certificate certificate =
          CertificateUtil.decodeCertificate(issued.certificate().bytesOrEmpty());
      assertEquals(keyPair.getPublic(), certificate.getPublicKey());
      assertEquals(APPLICATION_URI, CertificateUtil.getSanUri(certificate).orElse(null));
      certificate.verify(gds.getCaCertificate().getPublicKey());
      assertNull(issued.privateKey(), "signing requests never return a private key");
      assertEquals(
          List.of(gds.getCaCertificate()),
          CertificateUtil.decodeCertificates(issued.issuerCertificates()[0].bytesOrEmpty()));
    }

    @Test
    void finishRequestOfARejectedRequestFailsWithBadRequestNotAllowed() throws Exception {
      gds.setRejectRequests(true);
      NodeId applicationId = registerTestApplication();
      KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
      NodeId requestId =
          gdsClient.startSigningRequest(applicationId, null, null, csr(keyPair, APPLICATION_URI));

      UaException e =
          assertThrows(UaException.class, () -> gdsClient.finishRequest(applicationId, requestId));

      assertEquals(StatusCodes.Bad_RequestNotAllowed, e.getStatusCode().value());
    }

    @Test
    void startNewKeyPairRequestIsFinishedWithACertificateAndPrivateKey() throws Exception {
      NodeId applicationId = registerTestApplication();

      NodeId requestId =
          gdsClient.startNewKeyPairRequest(
              applicationId, null, null, "CN=Test", new String[] {"localhost"}, "PEM", null);
      FinishRequestResult issued = gdsClient.finishRequest(applicationId, requestId);

      ByteString privateKey = issued.privateKey();
      assertNotNull(privateKey);
      assertFalse(privateKey.isNullOrEmpty(), "key pair requests return a private key");
      assertEquals(
          APPLICATION_URI,
          CertificateUtil.getSanUri(
                  CertificateUtil.decodeCertificate(issued.certificate().bytesOrEmpty()))
              .orElse(null));
    }

    @Test
    void getCertificatesListsTheCertificatesIssuedToTheApplication() throws Exception {
      NodeId applicationId = registerTestApplication();
      KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
      NodeId requestId =
          gdsClient.startSigningRequest(applicationId, null, null, csr(keyPair, APPLICATION_URI));
      FinishRequestResult issued = gdsClient.finishRequest(applicationId, requestId);

      CertificatesResult certificates = gdsClient.getCertificates(applicationId, null);

      assertArrayEquals(
          new NodeId[] {NodeIds.RsaSha256ApplicationCertificateType},
          certificates.certificateTypeIds());
      assertArrayEquals(new ByteString[] {issued.certificate()}, certificates.certificates());
    }

    @Test
    void revokeCertificateIsReflectedByCheckRevocationStatus() throws Exception {
      NodeId applicationId = registerTestApplication();
      KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
      NodeId requestId =
          gdsClient.startSigningRequest(applicationId, null, null, csr(keyPair, APPLICATION_URI));
      ByteString certificate = gdsClient.finishRequest(applicationId, requestId).certificate();

      RevocationStatus before = gdsClient.checkRevocationStatus(certificate);
      gdsClient.revokeCertificate(applicationId, certificate);
      RevocationStatus after = gdsClient.checkRevocationStatus(certificate);

      assertTrue(before.certificateStatus().isGood());
      assertEquals(StatusCodes.Bad_CertificateRevoked, after.certificateStatus().value());
      assertNotNull(after.validityTime());
    }

    @Test
    void verifyIssuedCertificateRejectsAKeyOrUriMismatch() throws Exception {
      KeyPair requested = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
      KeyPair other = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
      NodeId applicationId = registerTestApplication();
      NodeId requestId =
          gdsClient.startSigningRequest(applicationId, null, null, csr(requested, APPLICATION_URI));
      X509Certificate issued =
          CertificateUtil.decodeCertificate(
              gdsClient.finishRequest(applicationId, requestId).certificate().bytesOrEmpty());

      GdsClient.verifyIssuedCertificate(issued, requested.getPublic(), APPLICATION_URI);

      UaException wrongKey =
          assertThrows(
              UaException.class,
              () -> GdsClient.verifyIssuedCertificate(issued, other.getPublic(), APPLICATION_URI));
      UaException wrongUri =
          assertThrows(
              UaException.class,
              () -> GdsClient.verifyIssuedCertificate(issued, requested.getPublic(), "urn:other"));

      assertEquals(StatusCodes.Bad_CertificateInvalid, wrongKey.getStatusCode().value());
      assertEquals(StatusCodes.Bad_CertificateUriInvalid, wrongUri.getStatusCode().value());
    }
  }

  @Nested
  class PullSequence {

    // G1: the whole Part 12 §7.6 Pull sequence against the fake GDS ends with the CA that signed
    // the issued certificate installed in the client's trust list manager.
    @Test
    void fullPullSequenceEndsWithTheGdsCaInTheTrustListManager() throws Exception {
      gds.getApplicationGroupTrustList()
          .setTrustList(
              new TrustListDataType(
                  uint(TrustListMasks.TrustedCertificates.getValue()),
                  new ByteString[] {ByteString.of(gds.getCaCertificate().getEncoded())},
                  null,
                  null,
                  null));
      var trustListManager = new MemoryTrustListManager();
      KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);

      ApplicationRecordDataType[] found = gdsClient.findApplications(APPLICATION_URI);
      NodeId applicationId =
          found.length == 0
              ? gdsClient.registerApplication(clientRecord())
              : found[0].getApplicationId();

      NodeId groupId = gdsClient.getCertificateGroups(applicationId)[0];
      NodeId typeId = gdsClient.readCertificateTypes(groupId)[0];
      assertTrue(gdsClient.getCertificateStatus(applicationId, groupId, typeId));

      NodeId requestId =
          gdsClient.startSigningRequest(
              applicationId, groupId, typeId, csr(keyPair, APPLICATION_URI));
      FinishRequestResult issued = gdsClient.finishRequest(applicationId, requestId);
      X509Certificate certificate =
          CertificateUtil.decodeCertificate(issued.certificate().bytesOrEmpty());
      GdsClient.verifyIssuedCertificate(certificate, keyPair.getPublic(), APPLICATION_URI);

      NodeId trustListId = gdsClient.getTrustList(applicationId, groupId);
      TrustListDataType trustList = TrustListReader.read(client, trustListId);
      TrustListApplier.apply(trustList, trustListManager);

      assertEquals(List.of(gds.getCaCertificate()), trustListManager.getTrustedCertificates());
      assertEquals(
          certificate.getIssuerX500Principal(), gds.getCaCertificate().getSubjectX500Principal());
    }
  }
}
