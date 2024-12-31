/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

public class KeyStoreLoader {

  private static final String CLIENT_ALIAS = "client-ai";
  private static final String SERVER_ALIAS = "server-ai";
  private static final char[] PASSWORD = "password".toCharArray();

  private X509Certificate clientCertificate;
  private KeyPair clientKeyPair;
  private X509Certificate serverCertificate;
  private KeyPair serverKeyPair;

  public KeyStoreLoader load() throws Exception {
    KeyStore keyStore = KeyStore.getInstance("PKCS12");
    keyStore.load(getClass().getClassLoader().getResourceAsStream("example-certs.pfx"), PASSWORD);

    Key clientPrivateKey = keyStore.getKey(CLIENT_ALIAS, PASSWORD);
    if (clientPrivateKey instanceof PrivateKey) {
      clientCertificate = (X509Certificate) keyStore.getCertificate(CLIENT_ALIAS);
      PublicKey clientPublicKey = clientCertificate.getPublicKey();
      clientKeyPair = new KeyPair(clientPublicKey, (PrivateKey) clientPrivateKey);
    }

    Key serverPrivateKey = keyStore.getKey(SERVER_ALIAS, PASSWORD);
    if (serverPrivateKey instanceof PrivateKey) {
      serverCertificate = (X509Certificate) keyStore.getCertificate(SERVER_ALIAS);
      PublicKey serverPublicKey = serverCertificate.getPublicKey();
      serverKeyPair = new KeyPair(serverPublicKey, (PrivateKey) serverPrivateKey);
    }

    return this;
  }

  public X509Certificate getClientCertificate() {
    return clientCertificate;
  }

  public KeyPair getClientKeyPair() {
    return clientKeyPair;
  }

  public X509Certificate getServerCertificate() {
    return serverCertificate;
  }

  public KeyPair getServerKeyPair() {
    return serverKeyPair;
  }
}
