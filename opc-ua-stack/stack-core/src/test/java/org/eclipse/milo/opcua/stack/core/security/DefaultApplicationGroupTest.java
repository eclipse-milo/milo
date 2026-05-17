/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.List;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;

@NullMarked
class DefaultApplicationGroupTest {

  @Test
  void defaultConstructorSupportsRsaSha256Only() {
    DefaultApplicationGroup group =
        new DefaultApplicationGroup(
            new MemoryTrustListManager(),
            new MemoryCertificateStore(),
            new TestCertificateFactory(),
            new CertificateValidator.InsecureCertificateValidator());

    assertEquals(
        List.of(NodeIds.RsaSha256ApplicationCertificateType),
        group.getSupportedCertificateTypeIds());
  }

  @Test
  void configuredCertificateTypesAreInitializedAndAccessible() throws Exception {
    List<NodeId> certificateTypeIds =
        List.of(
            NodeIds.RsaSha256ApplicationCertificateType,
            NodeIds.EccNistP256ApplicationCertificateType,
            NodeIds.EccBrainpoolP384r1ApplicationCertificateType,
            NodeIds.EccCurve25519ApplicationCertificateType);
    DefaultApplicationGroup group =
        new DefaultApplicationGroup(
            new MemoryTrustListManager(),
            new MemoryCertificateStore(),
            new CurrentEccCertificateFactory(),
            new CertificateValidator.InsecureCertificateValidator(),
            certificateTypeIds);

    group.initialize();

    assertEquals(certificateTypeIds, group.getSupportedCertificateTypeIds());
    assertEquals(4, group.getCertificateEntries().size());
    assertTrue(group.getKeyPair(NodeIds.RsaSha256ApplicationCertificateType).isPresent());
    assertTrue(group.getKeyPair(NodeIds.EccNistP256ApplicationCertificateType).isPresent());
    assertTrue(group.getKeyPair(NodeIds.EccBrainpoolP384r1ApplicationCertificateType).isPresent());
    assertTrue(group.getKeyPair(NodeIds.EccCurve25519ApplicationCertificateType).isPresent());
    assertTrue(
        group.getCertificateChain(NodeIds.EccNistP256ApplicationCertificateType).isPresent());
    assertTrue(
        group
            .getCertificateChain(NodeIds.EccBrainpoolP384r1ApplicationCertificateType)
            .isPresent());
    assertTrue(
        group.getCertificateChain(NodeIds.EccCurve25519ApplicationCertificateType).isPresent());
  }

  @Test
  void updateCertificateRejectsUnsupportedType() {
    DefaultApplicationGroup group =
        new DefaultApplicationGroup(
            new MemoryTrustListManager(),
            new MemoryCertificateStore(),
            new TestCertificateFactory(),
            new CertificateValidator.InsecureCertificateValidator());
    TestCertificateFactory factory = new TestCertificateFactory();
    KeyPair keyPair = factory.createRsaSha256KeyPair();
    X509Certificate[] certificateChain = factory.createRsaSha256CertificateChain(keyPair);

    assertThrows(
        UaException.class,
        () ->
            group.updateCertificate(
                NodeIds.EccNistP256ApplicationCertificateType, keyPair, certificateChain));
    assertFalse(group.getKeyPair(NodeIds.EccNistP256ApplicationCertificateType).isPresent());
  }

  private static final class CurrentEccCertificateFactory extends TestCertificateFactory {

    @Override
    public KeyPair createKeyPair(NodeId nodeId) {
      try {
        if (nodeId.equals(NodeIds.EccNistP256ApplicationCertificateType)) {
          return createEccNistP256KeyPair();
        } else if (nodeId.equals(NodeIds.EccBrainpoolP384r1ApplicationCertificateType)) {
          return createEccBrainpoolP384r1KeyPair();
        } else if (nodeId.equals(NodeIds.EccCurve25519ApplicationCertificateType)) {
          return createEccCurve25519KeyPair();
        } else {
          return super.createKeyPair(nodeId);
        }
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    @Override
    public X509Certificate[] createCertificateChain(NodeId nodeId, KeyPair keyPair) {
      if (nodeId.equals(NodeIds.EccNistP256ApplicationCertificateType)
          || nodeId.equals(NodeIds.EccBrainpoolP384r1ApplicationCertificateType)
          || nodeId.equals(NodeIds.EccCurve25519ApplicationCertificateType)) {

        return createEccApplicationCertificateChain(keyPair);
      } else {
        return super.createCertificateChain(nodeId, keyPair);
      }
    }

    private X509Certificate[] createEccApplicationCertificateChain(KeyPair keyPair) {
      try {
        X509Certificate certificate =
            SelfSignedCertificateBuilder.forEccApplicationCertificate(keyPair)
                .setApplicationUri("urn:eclipse:milo:test")
                .addDnsName("localhost")
                .build();

        return new X509Certificate[] {certificate};
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }
}
