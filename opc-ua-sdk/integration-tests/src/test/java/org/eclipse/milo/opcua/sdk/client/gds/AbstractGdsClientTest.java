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

import java.security.KeyPair;
import java.util.List;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.gds.types.ApplicationRecordDataType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

/** A client/server test whose server hosts a {@link FakeGdsNamespace}. */
public abstract class AbstractGdsClientTest extends AbstractClientServerTest {

  protected static final String APPLICATION_URI = "urn:eclipse:milo:test:gds:application";

  protected FakeGdsNamespace gds;
  protected GdsClient gdsClient;

  @Override
  protected TestServer createTestServer() throws Exception {
    TestServer testServer = super.createTestServer();

    gds = new FakeGdsNamespace(testServer.getServer());
    gds.startup();

    return testServer;
  }

  @BeforeAll
  void createGdsClient() throws UaException {
    gdsClient = GdsClient.create(client);
  }

  @BeforeEach
  void resetGds() {
    gds.reset();
  }

  @AfterAll
  void shutdownGds() {
    gds.shutdown();
  }

  protected static ApplicationRecordDataType clientRecord() {
    return new ApplicationRecordDataType(
        NodeId.NULL_VALUE,
        APPLICATION_URI,
        ApplicationType.Client,
        new LocalizedText[] {LocalizedText.english("Milo GDS Test Client")},
        "urn:eclipse:milo:test:product",
        null,
        null);
  }

  protected NodeId registerTestApplication() throws UaException {
    return gdsClient.registerApplication(clientRecord());
  }

  protected static ByteString csr(KeyPair keyPair, String applicationUri) throws Exception {
    return ByteString.of(
        CertificateUtil.generateCsr(
                keyPair,
                "CN=Milo GDS Test Client",
                applicationUri,
                List.of(),
                List.of(),
                "SHA256withRSA")
            .getEncoded());
  }
}
