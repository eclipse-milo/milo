/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import org.eclipse.milo.opcua.stack.core.security.CertificateManager;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator;
import org.junit.jupiter.api.BeforeEach;

public abstract class SecurityFixture {

  private static final String CLIENT_ALIAS = "client-test-certificate";
  private static final String SERVER_ALIAS = "server-test-certificate";
  private static final String CLIENT_ALIAS_4096 = "client-4096";
  private static final String SERVER_ALIAS_4096 = "server-4096";
  private static final char[] PASSWORD = "test".toCharArray();

  protected volatile X509Certificate clientCertificate;
  protected volatile byte[] clientCertificateBytes;
  protected volatile KeyPair clientKeyPair;

  protected volatile X509Certificate clientCertificate4096;
  protected volatile byte[] clientCertificateBytes4096;
  protected volatile KeyPair clientKeyPair4096;

  protected volatile X509Certificate serverCertificate;
  protected volatile byte[] serverCertificateBytes;
  protected volatile KeyPair serverKeyPair;

  protected volatile X509Certificate serverCertificate4096;
  protected volatile byte[] serverCertificateBytes4096;
  protected volatile KeyPair serverKeyPair4096;

  protected volatile CertificateManager serverCertificateManager;
  protected volatile CertificateValidator serverCertificateValidator;

  @BeforeEach
  public void setUp() throws Exception {
    KeyStore keyStore = KeyStore.getInstance("PKCS12");

    keyStore.load(getClass().getClassLoader().getResourceAsStream("test-keystore.pfx"), PASSWORD);

    {
      Key clientPrivateKey = keyStore.getKey(CLIENT_ALIAS, PASSWORD);
      if (clientPrivateKey instanceof PrivateKey) {
        clientCertificate = (X509Certificate) keyStore.getCertificate(CLIENT_ALIAS);
        clientCertificateBytes = clientCertificate.getEncoded();

        PublicKey clientPublicKey = clientCertificate.getPublicKey();
        clientKeyPair = new KeyPair(clientPublicKey, (PrivateKey) clientPrivateKey);
      }
    }

    {
      Key clientPrivateKey4096 = keyStore.getKey(CLIENT_ALIAS_4096, PASSWORD);
      if (clientPrivateKey4096 instanceof PrivateKey) {
        clientCertificate4096 = (X509Certificate) keyStore.getCertificate(CLIENT_ALIAS_4096);
        clientCertificateBytes4096 = clientCertificate4096.getEncoded();

        PublicKey clientPublicKey = clientCertificate4096.getPublicKey();
        clientKeyPair4096 = new KeyPair(clientPublicKey, (PrivateKey) clientPrivateKey4096);
      }
    }

    {
      Key serverPrivateKey = keyStore.getKey(SERVER_ALIAS, PASSWORD);
      if (serverPrivateKey instanceof PrivateKey) {
        serverCertificate = (X509Certificate) keyStore.getCertificate(SERVER_ALIAS);
        serverCertificateBytes = serverCertificate.getEncoded();

        PublicKey serverPublicKey = serverCertificate.getPublicKey();
        serverKeyPair = new KeyPair(serverPublicKey, (PrivateKey) serverPrivateKey);
      }
    }

    {
      Key serverPrivateKey4096 = keyStore.getKey(SERVER_ALIAS_4096, PASSWORD);
      if (serverPrivateKey4096 instanceof PrivateKey) {
        serverCertificate4096 = (X509Certificate) keyStore.getCertificate(SERVER_ALIAS_4096);
        serverCertificateBytes4096 = serverCertificate4096.getEncoded();

        PublicKey serverPublicKey = serverCertificate4096.getPublicKey();
        serverKeyPair4096 = new KeyPair(serverPublicKey, (PrivateKey) serverPrivateKey4096);
      }
    }

    serverCertificateValidator = new TestServerCertificateValidator(clientCertificate);

    serverCertificateManager =
        new TestCertificateManager(serverKeyPair, serverCertificate, serverCertificateValidator);
  }
}
