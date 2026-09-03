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
import java.util.Objects;
import java.util.Optional;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OPC UA application certificate group backed by a {@link CertificateStore}.
 *
 * <p>The group reads and writes its {@link CertificateStore} directly and has no lifecycle of its
 * own: it is usable as soon as it is constructed, and it exposes whatever certificate material the
 * store holds for its supported certificate types.
 *
 * <p>When the store is missing material for a supported certificate type, {@link
 * #createMissingCertificates()} uses the {@link CertificateFactory} to generate it. This is a
 * convenience for applications that want Milo to bootstrap a self-signed identity. Applications
 * whose certificates are provisioned externally, for example by a GDS or deployment tooling, never
 * need to call it.
 *
 * <p>By default, this group supports the {@link NodeIds#RsaSha256ApplicationCertificateType}
 * CertificateType, which can be used with 2048- and 4096-bit RSA keys. Callers can configure
 * additional certificate type IDs and an application-defined group ID.
 *
 * <p>Example, bootstrapping a self-signed identity when the store is empty:
 *
 * <pre>{@code
 * var group =
 *     new DefaultApplicationGroup(
 *         trustListManager, certificateStore, certificateFactory, certificateValidator);
 *
 * group.createMissingCertificates();
 * }</pre>
 *
 * <p>Example, using material an external authority has already placed in the store:
 *
 * <pre>{@code
 * var group =
 *     new DefaultApplicationGroup(
 *         trustListManager, certificateStore, certificateFactory, certificateValidator);
 * }</pre>
 */
public class DefaultApplicationGroup implements CertificateGroup {

  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultApplicationGroup.class);

  private final NodeId certificateGroupId;
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
        NodeIds.ServerConfiguration_CertificateGroups_DefaultApplicationGroup,
        trustListManager,
        certificateStore,
        certificateFactory,
        certificateValidator);
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

    this(
        NodeIds.ServerConfiguration_CertificateGroups_DefaultApplicationGroup,
        trustListManager,
        certificateStore,
        certificateFactory,
        certificateValidator,
        supportedCertificateTypeIds);
  }

  /**
   * Create an application-defined group for RSA SHA-256 application certificates.
   *
   * @param certificateGroupId the {@link NodeId} identifying this group.
   * @param trustListManager the {@link TrustListManager} for this group.
   * @param certificateStore the {@link CertificateStore} for local certificate material.
   * @param certificateFactory the {@link CertificateFactory} for missing certificates.
   * @param certificateValidator the {@link CertificateValidator} for remote certificates.
   */
  public DefaultApplicationGroup(
      NodeId certificateGroupId,
      TrustListManager trustListManager,
      CertificateStore certificateStore,
      CertificateFactory certificateFactory,
      CertificateValidator certificateValidator) {

    this(
        certificateGroupId,
        trustListManager,
        certificateStore,
        certificateFactory,
        certificateValidator,
        List.of(NodeIds.RsaSha256ApplicationCertificateType));
  }

  /**
   * Create an application-defined group for the configured certificate type IDs.
   *
   * @param certificateGroupId the {@link NodeId} identifying this group.
   * @param trustListManager the {@link TrustListManager} for this group.
   * @param certificateStore the {@link CertificateStore} for local certificate material.
   * @param certificateFactory the {@link CertificateFactory} for missing certificates.
   * @param certificateValidator the {@link CertificateValidator} for remote certificates.
   * @param supportedCertificateTypeIds the certificate type IDs this group supports.
   * @throws IllegalArgumentException if {@code supportedCertificateTypeIds} is empty.
   */
  public DefaultApplicationGroup(
      NodeId certificateGroupId,
      TrustListManager trustListManager,
      CertificateStore certificateStore,
      CertificateFactory certificateFactory,
      CertificateValidator certificateValidator,
      List<NodeId> supportedCertificateTypeIds) {

    this.certificateGroupId = Objects.requireNonNull(certificateGroupId, "certificateGroupId");
    this.trustListManager = trustListManager;
    this.certificateStore = certificateStore;
    this.certificateFactory = certificateFactory;
    this.certificateValidator = certificateValidator;
    this.supportedCertificateTypeIds = List.copyOf(supportedCertificateTypeIds);

    if (this.supportedCertificateTypeIds.isEmpty()) {
      throw new IllegalArgumentException("supportedCertificateTypeIds must not be empty");
    }
  }

  /**
   * Create certificate material for every supported certificate type the {@link CertificateStore}
   * is missing.
   *
   * <p>For each missing type, the {@link CertificateFactory} creates a key pair and certificate
   * chain, which are then stored. Types that already have an entry are left untouched. The call is
   * idempotent and may be repeated, for example after a failure or after an entry has been removed
   * from the store.
   *
   * @return the certificate type IDs for which material was created, in supported-type order; empty
   *     if the store already held every supported type.
   * @throws Exception if the store or the factory fails for any certificate type. Material created
   *     for earlier types before the failure remains in the store.
   */
  public synchronized List<NodeId> createMissingCertificates() throws Exception {
    var created = new ArrayList<NodeId>();

    for (NodeId certificateTypeId : getSupportedCertificateTypeIds()) {
      if (!certificateStore.contains(certificateTypeId)) {
        KeyPair keyPair = certificateFactory.createKeyPair(certificateTypeId);
        X509Certificate[] certificateChain =
            certificateFactory.createCertificateChain(certificateTypeId, keyPair);

        certificateStore.set(
            certificateTypeId, new CertificateStore.Entry(keyPair.getPrivate(), certificateChain));

        created.add(certificateTypeId);
      }
    }

    return List.copyOf(created);
  }

  /**
   * Create certificate material for every supported certificate type the store is missing.
   *
   * @throws Exception if the store or the factory fails for any certificate type.
   * @deprecated use {@link #createMissingCertificates()}. The group has no initialization step;
   *     this method only creates missing certificate material.
   */
  @Deprecated
  public void initialize() throws Exception {
    createMissingCertificates();
  }

  @Override
  public NodeId getCertificateGroupId() {
    return certificateGroupId;
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
        // A failure for one certificate type (e.g. a bad ECC alias password or a corrupt entry)
        // must not discard healthy entries already collected for other types; keep accumulating
        // so a single bad entry can't disable the group's other identities.
        LOGGER.warn(
            "Failed to read certificate entry for certificateTypeId={}", certificateTypeId, e);
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
   * Create a default application group and create any certificate material the store is missing.
   *
   * @param trustListManager the {@link TrustListManager} to use.
   * @param certificateStore the {@link CertificateStore} to use.
   * @param certificateFactory the {@link CertificateFactory} to use.
   * @param certificateValidator the {@link CertificateValidator} to use.
   * @return a {@link DefaultApplicationGroup} whose store holds material for every supported
   *     certificate type.
   * @throws Exception if creating or storing missing certificate material fails.
   * @deprecated construct the group directly, then call {@link #createMissingCertificates()} if
   *     Milo should generate material the store is missing. Omit that call when certificates are
   *     provisioned externally.
   */
  @Deprecated
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
   * Create a default application group and create any certificate material the store is missing.
   *
   * @param trustListManager the {@link TrustListManager} to use.
   * @param certificateStore the {@link CertificateStore} to use.
   * @param certificateFactory the {@link CertificateFactory} to use.
   * @param certificateValidator the {@link CertificateValidator} to use.
   * @param supportedCertificateTypeIds the certificate type IDs this group supports.
   * @return a {@link DefaultApplicationGroup} whose store holds material for every supported
   *     certificate type.
   * @throws Exception if creating or storing missing certificate material fails.
   * @deprecated construct the group directly, then call {@link #createMissingCertificates()} if
   *     Milo should generate material the store is missing. Omit that call when certificates are
   *     provisioned externally.
   */
  @Deprecated
  public static DefaultApplicationGroup createAndInitialize(
      TrustListManager trustListManager,
      CertificateStore certificateStore,
      CertificateFactory certificateFactory,
      CertificateValidator certificateValidator,
      List<NodeId> supportedCertificateTypeIds)
      throws Exception {

    var group =
        new DefaultApplicationGroup(
            trustListManager,
            certificateStore,
            certificateFactory,
            certificateValidator,
            supportedCertificateTypeIds);

    group.createMissingCertificates();

    return group;
  }
}
