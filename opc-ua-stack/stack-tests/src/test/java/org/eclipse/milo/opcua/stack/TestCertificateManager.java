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

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Optional;
import org.eclipse.milo.opcua.stack.core.security.CertificateGroup;
import org.eclipse.milo.opcua.stack.core.security.CertificateManager;
import org.eclipse.milo.opcua.stack.core.security.CertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.DefaultApplicationGroup;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateStore;
import org.eclipse.milo.opcua.stack.core.security.MemoryTrustListManager;
import org.eclipse.milo.opcua.stack.core.security.RsaSha256CertificateFactory;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

public class TestCertificateManager implements CertificateManager {

  private final MemoryCertificateQuarantine certificateQuarantine =
      new MemoryCertificateQuarantine();

  private final KeyPair keyPair;
  private final X509Certificate certificate;
  private final DefaultApplicationGroup certificateGroup;

  public TestCertificateManager(
      KeyPair keyPair, X509Certificate certificate, CertificateValidator certificateValidator)
      throws Exception {

    this.keyPair = keyPair;
    this.certificate = certificate;

    certificateGroup =
        DefaultApplicationGroup.createAndInitialize(
            new MemoryTrustListManager(),
            new MemoryCertificateStore(),
            new RsaSha256CertificateFactory() {
              @Override
              protected KeyPair createRsaSha256KeyPair() {
                return keyPair;
              }

              @Override
              protected X509Certificate[] createRsaSha256CertificateChain(KeyPair keyPair) {
                return new X509Certificate[] {certificate};
              }
            },
            certificateValidator);
  }

  @Override
  public Optional<KeyPair> getKeyPair(ByteString thumbprint) {
    return Optional.of(keyPair);
  }

  @Override
  public Optional<X509Certificate> getCertificate(ByteString thumbprint) {
    return Optional.of(certificate);
  }

  @Override
  public Optional<X509Certificate[]> getCertificateChain(ByteString thumbprint) {
    return getCertificate(thumbprint).map(c -> new X509Certificate[] {c});
  }

  @Override
  public Optional<CertificateGroup> getCertificateGroup(ByteString thumbprint) {
    return Optional.of(certificateGroup);
  }

  @Override
  public Optional<CertificateGroup> getCertificateGroup(NodeId certificateGroupId) {
    return Optional.of(certificateGroup);
  }

  @Override
  public List<CertificateGroup> getCertificateGroups() {
    return List.of(certificateGroup);
  }

  @Override
  public CertificateQuarantine getCertificateQuarantine() {
    return certificateQuarantine;
  }
}
