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
 * Default OPC UA application certificate group backed by a {@link CertificateStore}.
 *
 * <p>By default, this group supports the {@link NodeIds#RsaSha256ApplicationCertificateType}
 * CertificateType, which can be used with 2048- and 4096-bit RSA keys. Callers can configure
 * additional certificate type IDs when a server manages multiple application identities.
 */
public class DefaultApplicationGroup implements CertificateGroup {

  private final AtomicBoolean initialized = new AtomicBoolean(false);

  private final CertificateValidator certificateValidator;

  private final TrustListManager trustListManager;
  private final CertificateStore certificateStore;
  private final CertificateFactory certificateFactory;
  private final List<NodeId> supportedCertificateTypeIds;

  /**
   * Create a default application group for RSA SHA-256 application certificates.
   *
   * @param trustListManager the {@link TrustListManager} for this group.
   * @param certificateStore the {@link CertificateStore} for local certificate material.
   * @param certificateFactory the {@link CertificateFactory} for missing certificates.
   * @param certificateValidator the {@link CertificateValidator} for remote certificates.
   */
  public DefaultApplicationGroup(
      TrustListManager trustListManager,
      CertificateStore certificateStore,
      CertificateFactory certificateFactory,
      CertificateValidator certificateValidator) {

    this(
        trustListManager,
        certificateStore,
        certificateFactory,
        certificateValidator,
        List.of(NodeIds.RsaSha256ApplicationCertificateType));
  }

  /**
   * Create a default application group for the configured certificate type IDs.
   *
   * @param trustListManager the {@link TrustListManager} for this group.
   * @param certificateStore the {@link CertificateStore} for local certificate material.
   * @param certificateFactory the {@link CertificateFactory} for missing certificates.
   * @param certificateValidator the {@link CertificateValidator} for remote certificates.
   * @param supportedCertificateTypeIds the certificate type IDs this group supports.
   * @throws IllegalArgumentException if {@code supportedCertificateTypeIds} is empty.
   */
  public DefaultApplicationGroup(
      TrustListManager trustListManager,
      CertificateStore certificateStore,
      CertificateFactory certificateFactory,
      CertificateValidator certificateValidator,
      List<NodeId> supportedCertificateTypeIds) {

    this.trustListManager = trustListManager;
    this.certificateStore = certificateStore;
    this.certificateFactory = certificateFactory;
    this.certificateValidator = certificateValidator;
    this.supportedCertificateTypeIds = List.copyOf(supportedCertificateTypeIds);

    if (this.supportedCertificateTypeIds.isEmpty()) {
      throw new IllegalArgumentException("supportedCertificateTypeIds must not be empty");
    }
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
    return supportedCertificateTypeIds;
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
    if (supportsCertificateType(certificateTypeId)) {
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
    if (supportsCertificateType(certificateTypeId)) {
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

    if (supportsCertificateType(certificateTypeId)) {
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

  private boolean supportsCertificateType(NodeId certificateTypeId) {
    return supportedCertificateTypeIds.contains(certificateTypeId);
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

    return createAndInitialize(
        trustListManager,
        certificateStore,
        certificateFactory,
        certificateValidator,
        List.of(NodeIds.RsaSha256ApplicationCertificateType));
  }

  /**
   * Create and initialize a {@link DefaultApplicationGroup}.
   *
   * @param trustListManager the {@link TrustListManager} to use.
   * @param certificateStore the {@link CertificateStore} to use.
   * @param certificateFactory the {@link CertificateFactory} to use.
   * @param certificateValidator the {@link CertificateValidator} to use.
   * @param supportedCertificateTypeIds the certificate type IDs this group supports.
   * @return an initialized {@link DefaultApplicationGroup} instance.
   * @throws Exception if an error occurs while initializing the {@link DefaultApplicationGroup}.
   */
  public static DefaultApplicationGroup createAndInitialize(
      TrustListManager trustListManager,
      CertificateStore certificateStore,
      CertificateFactory certificateFactory,
      CertificateValidator certificateValidator,
      List<NodeId> supportedCertificateTypeIds)
      throws Exception {

    var defaultApplicationGroup =
        new DefaultApplicationGroup(
            trustListManager,
            certificateStore,
            certificateFactory,
            certificateValidator,
            supportedCertificateTypeIds);

    defaultApplicationGroup.initialize();

    return defaultApplicationGroup;
  }
}
