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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.examples.client.GdsPullExample;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfigBuilder;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateGroup;
import org.eclipse.milo.opcua.stack.core.security.DefaultClientCertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.MemoryTrustListManager;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TrustListMasks;
import org.eclipse.milo.opcua.stack.core.types.structured.TrustListDataType;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/** Executes the original Pull example body against a real local GDS service fixture. */
@Timeout(30)
class WikiSourceGdsExampleTest extends AbstractGdsClientTest {
  @Override
  protected void customizeClientConfig(OpcUaClientConfigBuilder builder) {
    var trust = new MemoryTrustListManager();
    var quarantine = new MemoryCertificateQuarantine();
    var validator = new DefaultClientCertificateValidator(trust, quarantine);
    builder
        .setApplicationUri(
            CertificateUtil.getSanUri(testServer.getClientCertificate()).orElseThrow())
        .setCertificateGroup(
            DefaultCertificateGroup.forIdentity(
                testServer.getClientKeyPair(),
                testServer.getClientCertificateChain(),
                trust,
                quarantine,
                validator))
        .setCertificateValidator(validator);
  }

  // Calling the source method itself prevents this check from drifting into a rewritten workflow.
  @Test
  void originalPullBodyRegistersPollsAndInstallsTrustWithoutInstallingItsThrowawayIdentity()
      throws Exception {
    gds.setPollsBeforeIssued(1);
    gds.getApplicationGroupTrustList()
        .setTrustList(
            new TrustListDataType(
                uint(TrustListMasks.TrustedCertificates.getValue()),
                new ByteString[] {ByteString.of(gds.getCaCertificate().getEncoded())},
                null,
                null,
                null));
    var group = client.getConfig().getCertificateGroup().orElseThrow();
    var originalCertificate = group.getCertificateIdentities().get(0).certificate();
    var completion = new CompletableFuture<OpcUaClient>();
    new GdsPullExample().run(client, completion);
    assertSame(client, completion.get(5, TimeUnit.SECONDS));
    assertEquals(1, gds.getRegisterApplicationCallCount());
    assertEquals(1, gds.getStartSigningRequestCallCount());
    assertEquals(2, gds.getFinishRequestCallCount());
    assertEquals(
        List.of(gds.getCaCertificate()), group.getTrustListManager().getTrustedCertificates());
    assertEquals(originalCertificate, group.getCertificateIdentities().get(0).certificate());
    assertEquals(0, gds.getApplicationGroupTrustList().openHandles());

    gds.setPollsBeforeIssued(0);
    new GdsPullExample().run(client, new CompletableFuture<>());
    assertEquals(
        1, gds.getRegisterApplicationCallCount(), "the original body reuses its registration");
    assertEquals(2, gds.getStartSigningRequestCallCount());
    assertEquals(3, gds.getFinishRequestCallCount());
  }

  @Test
  void originalPullBodyPropagatesRejectedIssuanceWithoutCompletingSuccessfully() throws Exception {
    gds.setRejectRequests(true);
    var completion = new CompletableFuture<OpcUaClient>();
    var group = client.getConfig().getCertificateGroup().orElseThrow();
    var before = group.getTrustListManager().getSnapshot();
    UaException failure =
        assertThrows(UaException.class, () -> new GdsPullExample().run(client, completion));
    assertEquals(StatusCodes.Bad_RequestNotAllowed, failure.getStatusCode().value());
    assertFalse(completion.isDone());
    assertEquals(before, group.getTrustListManager().getSnapshot());
    assertEquals(0, gds.getApplicationGroupTrustList().openHandles());
  }
}
