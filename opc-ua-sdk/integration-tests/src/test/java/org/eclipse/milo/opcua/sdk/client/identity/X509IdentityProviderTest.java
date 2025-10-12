/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.identity;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfigBuilder;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.sdk.test.TestClient;
import org.eclipse.milo.opcua.sdk.test.TestServer.TestIdentityCertificate;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.junit.jupiter.api.Test;

public class X509IdentityProviderTest extends AbstractClientServerTest {

  @Test
  public void testX509IdentityProviderConnection() throws Exception {
    var session = client.getSession();
    assertNotNull(session);
  }

  @Test
  public void testX509IdentityProviderWithUntrustedCertificate() throws Exception {
    KeyPair untrustedKeyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    SelfSignedCertificateBuilder certBuilder = new SelfSignedCertificateBuilder(untrustedKeyPair);
    certBuilder.setCommonName("UntrustedIdentity");
    certBuilder.setApplicationUri("urn:eclipse:milo:test:untrusted");
    X509Certificate untrustedCertificate = certBuilder.build();

    OpcUaClient untrustedClient =
        TestClient.create(
            server,
            configBuilder ->
                configBuilder.setIdentityProvider(
                    new X509IdentityProvider(untrustedCertificate, untrustedKeyPair.getPrivate())));

    assertThrows(UaException.class, untrustedClient::connect);

    untrustedClient.disconnect();
  }

  @Override
  protected void customizeClientConfig(OpcUaClientConfigBuilder configBuilder) {
    TestIdentityCertificate identityCert = testServer.getIdentityCertificate1();
    configBuilder.setIdentityProvider(
        new X509IdentityProvider(identityCert.certificate(), identityCert.keyPair().getPrivate()));
  }
}
