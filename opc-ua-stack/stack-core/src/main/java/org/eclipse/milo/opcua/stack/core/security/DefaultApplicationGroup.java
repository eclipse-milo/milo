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

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

/**
 * An implementation of the DefaultApplicationGroup CertificateGroup.
 *
 * <p>Supports the {@link NodeIds#RsaSha256ApplicationCertificateType} CertificateType, which can be
 * used with 2048- and 4096-bit RSA keys.
 */
public class DefaultApplicationGroup implements CertificateGroup {

  private final AtomicBoolean initialized = new AtomicBoolean(false);

  private final CertificateValidator certificateValidator;

  private final TrustListManager trustListManager;
  private final CertificateStore certificateStore;
  private final CertificateFactory certificateFactory;

  public DefaultApplicationGroup(
      TrustListManager trustListManager,
      CertificateStore certificateStore,
      CertificateFactory certificateFactory,
      CertificateValidator certificateValidator) {

    this.trustListManager = trustListManager;
    this.certificateStore = certificateStore;
    this.certificateFactory = certificateFactory;
    this.certificateValidator = certificateValidator;
  }

  public void initialize() throws Exception {
    if (initialized.compareAndSet(false, true)) {
      for (NodeId certificateTypeId : getSupportedCertificateTypeIds()) {
        if (!certificateStore.contains(certificateTypeId)) {
          KeyPair keyPair = certificateFactory.createKeyPair(certificateTypeId);
          X509Certificate[] certificateChain =
              certificateFactory.createCertificateChain(certificateTypeId, keyPair);

          certificateStore.set(
              certificateTypeId,
              new CertificateStore.Entry(keyPair.getPrivate(), certificateChain));
        }
      }
    }
  }

  @Override
  public NodeId getCertificateGroupId() {
    return NodeIds.ServerConfiguration_CertificateGroups_DefaultApplicationGroup;
  }

  @Override
  public List<NodeId> getSupportedCertificateTypeIds() {
    return List.of(NodeIds.RsaSha256ApplicationCertificateType);
  }

  @Override
  public TrustListManager getTrustListManager() {
    return trustListManager;
  }

  @Override
  public List<Entry> getCertificateEntries() {
    var entries = new ArrayList<Entry>();

    for (NodeId certificateTypeId : getSupportedCertificateTypeIds()) {
      try {
        CertificateStore.Entry entry = certificateStore.get(certificateTypeId);

        if (entry != null) {
          entries.add(
              new CertificateGroup.Entry(
                  getCertificateGroupId(), certificateTypeId, entry.certificateChain));
        }
      } catch (Exception e) {
        return List.of();
      }
    }

    return entries;
  }

  @Override
  public Optional<KeyPair> getKeyPair(NodeId certificateTypeId) {
    if (certificateTypeId.equals(NodeIds.RsaSha256ApplicationCertificateType)) {
      try {
        CertificateStore.Entry entry = certificateStore.get(certificateTypeId);

        return Optional.ofNullable(entry)
            .map(r -> new KeyPair(r.certificateChain[0].getPublicKey(), r.privateKey));
      } catch (Exception e) {
        return Optional.empty();
      }
    } else {
      return Optional.empty();
    }
  }

  @Override
  public Optional<X509Certificate[]> getCertificateChain(NodeId certificateTypeId) {
    if (certificateTypeId.equals(NodeIds.RsaSha256ApplicationCertificateType)) {
      try {
        CertificateStore.Entry entry = certificateStore.get(certificateTypeId);

        return Optional.ofNullable(entry).map(r -> r.certificateChain);
      } catch (Exception e) {
        return Optional.empty();
      }
    } else {
      return Optional.empty();
    }
  }

  @Override
  public void updateCertificate(
      NodeId certificateTypeId, KeyPair keyPair, X509Certificate[] certificateChain)
      throws Exception {

    if (certificateTypeId.equals(NodeIds.RsaSha256ApplicationCertificateType)) {
      certificateStore.set(
          certificateTypeId, new CertificateStore.Entry(keyPair.getPrivate(), certificateChain));
    } else {
      throw new UaException(StatusCodes.Bad_InvalidArgument, "certificateTypeId");
    }
  }

  @Override
  public CertificateFactory getCertificateFactory() {
    return certificateFactory;
  }

  @Override
  public CertificateValidator getCertificateValidator() {
    return certificateValidator;
  }

  /**
   * Create and initialize a {@link DefaultApplicationGroup}.
   *
   * @param trustListManager the {@link TrustListManager} to use.
   * @param certificateStore the {@link CertificateStore} to use.
   * @param certificateFactory the {@link CertificateFactory} to use.
   * @param certificateValidator the {@link CertificateValidator} to use.
   * @return an initialized {@link DefaultApplicationGroup} instance.
   * @throws Exception if an error occurs while initializing the {@link DefaultApplicationGroup}.
   */
  public static DefaultApplicationGroup createAndInitialize(
      TrustListManager trustListManager,
      CertificateStore certificateStore,
      CertificateFactory certificateFactory,
      CertificateValidator certificateValidator)
      throws Exception {

    var defaultApplicationGroup =
        new DefaultApplicationGroup(
            trustListManager, certificateStore, certificateFactory, certificateValidator);

    defaultApplicationGroup.initialize();

    return defaultApplicationGroup;
  }
}
