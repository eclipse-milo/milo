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
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.CertificateGroup.Entry;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;

public class DefaultCertificateManager implements CertificateManager {

  private final Map<NodeId, CertificateGroup> certificateGroups = new ConcurrentHashMap<>();

  private final CertificateQuarantine certificateQuarantine;

  public DefaultCertificateManager(CertificateQuarantine certificateQuarantine) {
    this.certificateQuarantine = certificateQuarantine;
  }

  public DefaultCertificateManager(
      CertificateQuarantine certificateQuarantine, CertificateGroup group) {
    this(certificateQuarantine, List.of(group));
  }

  public DefaultCertificateManager(
      CertificateQuarantine certificateQuarantine, Collection<CertificateGroup> groups) {
    this.certificateQuarantine = certificateQuarantine;

    groups.forEach(g -> certificateGroups.put(g.getCertificateGroupId(), g));
  }

  @Override
  public Optional<KeyPair> getKeyPair(ByteString thumbprint) {
    return firstMatchingEntry(thumbprint)
        .flatMap(
            entry -> {
              Optional<CertificateGroup> group = getCertificateGroup(entry.certificateGroupId);
              return group.flatMap(g -> g.getKeyPair(entry.certificateTypeId));
            });
  }

  @Override
  public Optional<X509Certificate> getCertificate(ByteString thumbprint) {
    return firstMatchingEntry(thumbprint).map(e -> e.certificateChain[0]);
  }

  @Override
  public Optional<X509Certificate[]> getCertificateChain(ByteString thumbprint) {
    return firstMatchingEntry(thumbprint).map(e -> e.certificateChain);
  }

  @Override
  public Optional<CertificateGroup> getCertificateGroup(ByteString thumbprint) {
    return firstMatchingEntry(thumbprint).flatMap(r -> getCertificateGroup(r.certificateGroupId));
  }

  @Override
  public Optional<CertificateGroup> getCertificateGroup(NodeId certificateGroupId) {
    return Optional.ofNullable(certificateGroups.get(certificateGroupId));
  }

  @Override
  public List<CertificateGroup> getCertificateGroups() {
    return List.copyOf(certificateGroups.values());
  }

  @Override
  public CertificateQuarantine getCertificateQuarantine() {
    return certificateQuarantine;
  }

  private Optional<Entry> firstMatchingEntry(ByteString thumbprint) {
    return certificateGroups.values().stream()
        .flatMap(group -> group.getCertificateEntries().stream())
        .filter(
            entry -> {
              try {
                return CertificateUtil.thumbprint(entry.certificateChain[0]).equals(thumbprint);
              } catch (UaException e) {
                return false;
              }
            })
        .findFirst();
  }
}
