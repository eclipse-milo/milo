/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.security;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

abstract class CertificateStoreTest {

  protected CertificateStore certificateStore;

  @BeforeEach
  void setUpCertificateStore() throws Exception {
    var factory = new TestCertificateFactory();
    KeyPair keyPair = factory.createRsaSha256KeyPair();
    X509Certificate[] certificateChain = factory.createRsaSha256CertificateChain(keyPair);

    certificateStore = newCertificateStore();

    certificateStore.set(
        id("test"), new CertificateStore.Entry(keyPair.getPrivate(), certificateChain));
  }

  protected abstract CertificateStore newCertificateStore() throws Exception;

  @Test
  void contains() throws Exception {
    assertTrue(certificateStore.contains(id("test")));
    assertFalse(certificateStore.contains(id("foo")));
  }

  @Test
  void get() throws Exception {
    assertNotNull(certificateStore.get(id("test")));
    assertNull(certificateStore.get(id("foo")));
  }

  @Test
  void remove() throws Exception {
    assertNotNull(certificateStore.remove(id("test")));
    assertNull(certificateStore.remove(id("test")));
    assertFalse(certificateStore.contains(id("test")));
  }

  @Test
  void set() throws Exception {
    var factory = new TestCertificateFactory();
    KeyPair keyPair = factory.createRsaSha256KeyPair();
    X509Certificate[] certificateChain = factory.createRsaSha256CertificateChain(keyPair);

    certificateStore.set(
        id("test2"), new CertificateStore.Entry(keyPair.getPrivate(), certificateChain));

    assertTrue(certificateStore.contains(id("test2")));
  }

  @Test
  void supportsMultipleCertificateTypeIds() throws Exception {
    KeyPair nistP256KeyPair = SelfSignedCertificateGenerator.generateNistP256KeyPair();
    X509Certificate nistP256Certificate = buildEccApplicationCertificate(nistP256KeyPair);
    KeyPair nistP384KeyPair = SelfSignedCertificateGenerator.generateNistP384KeyPair();
    X509Certificate nistP384Certificate = buildEccApplicationCertificate(nistP384KeyPair);
    KeyPair brainpoolP256KeyPair = SelfSignedCertificateGenerator.generateBrainpoolP256r1KeyPair();
    X509Certificate brainpoolP256Certificate = buildEccApplicationCertificate(brainpoolP256KeyPair);
    KeyPair brainpoolP384KeyPair = SelfSignedCertificateGenerator.generateBrainpoolP384r1KeyPair();
    X509Certificate brainpoolP384Certificate = buildEccApplicationCertificate(brainpoolP384KeyPair);
    KeyPair ed25519KeyPair = SelfSignedCertificateGenerator.generateEd25519KeyPair();
    X509Certificate ed25519Certificate = buildEccApplicationCertificate(ed25519KeyPair);
    KeyPair ed448KeyPair = SelfSignedCertificateGenerator.generateEd448KeyPair();
    X509Certificate ed448Certificate = buildEccApplicationCertificate(ed448KeyPair);

    certificateStore.set(
        NodeIds.EccNistP256ApplicationCertificateType,
        new CertificateStore.Entry(
            nistP256KeyPair.getPrivate(), new X509Certificate[] {nistP256Certificate}));
    certificateStore.set(
        NodeIds.EccNistP384ApplicationCertificateType,
        new CertificateStore.Entry(
            nistP384KeyPair.getPrivate(), new X509Certificate[] {nistP384Certificate}));
    certificateStore.set(
        NodeIds.EccBrainpoolP256r1ApplicationCertificateType,
        new CertificateStore.Entry(
            brainpoolP256KeyPair.getPrivate(), new X509Certificate[] {brainpoolP256Certificate}));
    certificateStore.set(
        NodeIds.EccBrainpoolP384r1ApplicationCertificateType,
        new CertificateStore.Entry(
            brainpoolP384KeyPair.getPrivate(), new X509Certificate[] {brainpoolP384Certificate}));
    certificateStore.set(
        NodeIds.EccCurve25519ApplicationCertificateType,
        new CertificateStore.Entry(
            ed25519KeyPair.getPrivate(), new X509Certificate[] {ed25519Certificate}));
    certificateStore.set(
        NodeIds.EccCurve448ApplicationCertificateType,
        new CertificateStore.Entry(
            ed448KeyPair.getPrivate(), new X509Certificate[] {ed448Certificate}));

    assertTrue(certificateStore.contains(NodeIds.EccNistP256ApplicationCertificateType));
    assertTrue(certificateStore.contains(NodeIds.EccNistP384ApplicationCertificateType));
    assertTrue(certificateStore.contains(NodeIds.EccBrainpoolP256r1ApplicationCertificateType));
    assertTrue(certificateStore.contains(NodeIds.EccBrainpoolP384r1ApplicationCertificateType));
    assertTrue(certificateStore.contains(NodeIds.EccCurve25519ApplicationCertificateType));
    assertTrue(certificateStore.contains(NodeIds.EccCurve448ApplicationCertificateType));
    assertNotNull(certificateStore.get(NodeIds.EccNistP256ApplicationCertificateType));
    assertNotNull(certificateStore.get(NodeIds.EccNistP384ApplicationCertificateType));
    assertNotNull(certificateStore.get(NodeIds.EccBrainpoolP256r1ApplicationCertificateType));
    assertNotNull(certificateStore.get(NodeIds.EccBrainpoolP384r1ApplicationCertificateType));
    assertNotNull(certificateStore.get(NodeIds.EccCurve25519ApplicationCertificateType));
    assertNotNull(certificateStore.get(NodeIds.EccCurve448ApplicationCertificateType));
    assertNotNull(certificateStore.remove(NodeIds.EccNistP256ApplicationCertificateType));
    assertNotNull(certificateStore.remove(NodeIds.EccNistP384ApplicationCertificateType));
    assertNotNull(certificateStore.remove(NodeIds.EccBrainpoolP256r1ApplicationCertificateType));
    assertNotNull(certificateStore.remove(NodeIds.EccBrainpoolP384r1ApplicationCertificateType));
    assertNotNull(certificateStore.remove(NodeIds.EccCurve25519ApplicationCertificateType));
    assertNotNull(certificateStore.remove(NodeIds.EccCurve448ApplicationCertificateType));
  }

  private static NodeId id(String id) {
    return new NodeId(2, id);
  }

  private static X509Certificate buildEccApplicationCertificate(KeyPair keyPair) throws Exception {
    return SelfSignedCertificateBuilder.forEccApplicationCertificate(keyPair)
        .setApplicationUri("urn:eclipse:milo:test")
        .addDnsName("localhost")
        .build();
  }
}
