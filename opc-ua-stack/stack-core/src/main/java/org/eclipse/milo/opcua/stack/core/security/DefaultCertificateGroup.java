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
import java.util.Arrays;
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
 * store holds for its supported certificate types. It borrows its {@link TrustListManager}, {@link
 * CertificateStore}, and {@link CertificateQuarantine}; closing those resources remains the
 * application's responsibility.
 *
 * <p>By default, this group supports the {@link NodeIds#RsaSha256ApplicationCertificateType}
 * CertificateType, which can be used with 2048- and 4096-bit RSA keys. Callers can configure
 * additional certificate type IDs.
 *
 * <p>Example, a server group whose store may be empty on first start:
 *
 * <pre>{@code
 * var group =
 *     new DefaultCertificateGroup(
 *         trustListManager, certificateStore, certificateQuarantine, certificateValidator);
 *
 * certificateFactory.createMissingCertificates(group);
 *
 * var certificateManager = new DefaultCertificateManager(group);
 * }</pre>
 *
 * <p>Example, a client with one externally issued key pair and certificate chain:
 *
 * <pre>{@code
 * CertificateGroup group =
 *     DefaultCertificateGroup.forIdentity(
 *         keyPair, certificateChain, trustListManager, certificateQuarantine, certificateValidator);
 *
 * OpcUaClientConfig.builder().setCertificateGroup(group);
 * }</pre>
 */
public class DefaultCertificateGroup implements CertificateGroup {

  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCertificateGroup.class);

  private final TrustListManager trustListManager;
  private final CertificateStore certificateStore;
  private final CertificateQuarantine certificateQuarantine;
  private final CertificateValidator certificateValidator;
  private final List<NodeId> supportedCertificateTypeIds;

  /**
   * Create a group for RSA SHA-256 application certificates.
   *
   * @param trustListManager the {@link TrustListManager} for this group.
   * @param certificateStore the {@link CertificateStore} for local certificate material.
   * @param certificateQuarantine the {@link CertificateQuarantine} for rejected remote
   *     certificates.
   * @param certificateValidator the {@link CertificateValidator} for remote certificates.
   */
  public DefaultCertificateGroup(
      TrustListManager trustListManager,
      CertificateStore certificateStore,
      CertificateQuarantine certificateQuarantine,
      CertificateValidator certificateValidator) {

    this(
        trustListManager,
        certificateStore,
        certificateQuarantine,
        certificateValidator,
        List.of(NodeIds.RsaSha256ApplicationCertificateType));
  }

  /**
   * Create a group for the configured certificate type IDs.
   *
   * @param trustListManager the {@link TrustListManager} for this group.
   * @param certificateStore the {@link CertificateStore} for local certificate material.
   * @param certificateQuarantine the {@link CertificateQuarantine} for rejected remote
   *     certificates.
   * @param certificateValidator the {@link CertificateValidator} for remote certificates.
   * @param supportedCertificateTypeIds the certificate type IDs this group supports.
   * @throws IllegalArgumentException if {@code supportedCertificateTypeIds} is empty.
   */
  public DefaultCertificateGroup(
      TrustListManager trustListManager,
      CertificateStore certificateStore,
      CertificateQuarantine certificateQuarantine,
      CertificateValidator certificateValidator,
      List<NodeId> supportedCertificateTypeIds) {

    this.trustListManager = Objects.requireNonNull(trustListManager, "trustListManager");
    this.certificateStore = Objects.requireNonNull(certificateStore, "certificateStore");
    this.certificateQuarantine =
        Objects.requireNonNull(certificateQuarantine, "certificateQuarantine");
    this.certificateValidator =
        Objects.requireNonNull(certificateValidator, "certificateValidator");
    this.supportedCertificateTypeIds = List.copyOf(supportedCertificateTypeIds);

    if (this.supportedCertificateTypeIds.isEmpty()) {
      throw new IllegalArgumentException("supportedCertificateTypeIds must not be empty");
    }
  }

  /**
   * Create a group holding exactly one identity.
   *
   * <p>The certificate type is inferred from the leaf certificate's public key and signature
   * algorithm (see {@link CertificateCompatibility#inferCertificateTypeId(X509Certificate)}), and
   * the group supports only that type. The material is held in a {@link MemoryCertificateStore}.
   *
   * @param keyPair the key pair belonging to the leaf certificate.
   * @param certificateChain the leaf certificate and any issuer certificates.
   * @param trustListManager the {@link TrustListManager} for this group.
   * @param certificateQuarantine the {@link CertificateQuarantine} for rejected remote
   *     certificates.
   * @param certificateValidator the {@link CertificateValidator} for remote certificates.
   * @return a group whose only identity is {@code keyPair} and {@code certificateChain}.
   * @throws IllegalArgumentException if {@code certificateChain} is empty, the certificate type
   *     cannot be inferred from the leaf certificate, or the key pair's public key does not match
   *     the leaf certificate's public key.
   */
  public static DefaultCertificateGroup forIdentity(
      KeyPair keyPair,
      X509Certificate[] certificateChain,
      TrustListManager trustListManager,
      CertificateQuarantine certificateQuarantine,
      CertificateValidator certificateValidator) {

    if (certificateChain.length == 0) {
      throw new IllegalArgumentException("certificateChain must not be empty");
    }

    X509Certificate certificate = certificateChain[0];

    // Compare encodings rather than Key.equals: JDK and BouncyCastle key implementations do not
    // consider each other equal even when they encode the same key.
    if (!Arrays.equals(keyPair.getPublic().getEncoded(), certificate.getPublicKey().getEncoded())) {
      throw new IllegalArgumentException(
          "key pair public key does not match the leaf certificate public key");
    }

    NodeId certificateTypeId =
        CertificateCompatibility.inferCertificateTypeId(certificate)
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "cannot infer certificate type from public key algorithm '"
                            + certificate.getPublicKey().getAlgorithm()
                            + "'"));

    var certificateStore = new MemoryCertificateStore();
    try {
      certificateStore.set(
          certificateTypeId,
          new CertificateStore.Entry(keyPair.getPrivate(), certificateChain.clone()));
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }

    return new DefaultCertificateGroup(
        trustListManager,
        certificateStore,
        certificateQuarantine,
        certificateValidator,
        List.of(certificateTypeId));
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
  public CertificateQuarantine getCertificateQuarantine() {
    return certificateQuarantine;
  }

  @Override
  public List<Entry> getCertificateEntries() {
    var entries = new ArrayList<Entry>();

    for (NodeId certificateTypeId : getSupportedCertificateTypeIds()) {
      try {
        CertificateStore.Entry entry = certificateStore.get(certificateTypeId);

        if (entry != null) {
          entries.add(new CertificateGroup.Entry(certificateTypeId, entry.certificateChain));
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
  public boolean hasCertificate(NodeId certificateTypeId) throws Exception {
    return supportsCertificateType(certificateTypeId)
        && certificateStore.contains(certificateTypeId);
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
  public CertificateValidator getCertificateValidator() {
    return certificateValidator;
  }

  private boolean supportsCertificateType(NodeId certificateTypeId) {
    return supportedCertificateTypeIds.contains(certificateTypeId);
  }
}
