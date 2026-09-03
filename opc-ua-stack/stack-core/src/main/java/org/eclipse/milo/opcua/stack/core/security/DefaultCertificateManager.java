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

/**
 * A thread-safe, mutable registry of application-owned {@link CertificateGroup}s.
 *
 * <p>Adding a group atomically replaces any group with the same ID. Removing or replacing a group
 * does not initialize or close it; the application remains responsible for the lifecycle of every
 * group and its resources. Registry changes do not wait for aggregate lookups already in progress,
 * and each lookup remains bound to the group instance it observed.
 */
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

    groups.forEach(this::addCertificateGroup);
  }

  /**
   * Add a {@link CertificateGroup} to this manager.
   *
   * <p>If a group with the same ID is already registered, it is atomically replaced and returned
   * without being closed.
   *
   * @param group the application-owned group to add.
   * @return the replaced group, or empty if the ID was not registered.
   */
  public Optional<CertificateGroup> addCertificateGroup(CertificateGroup group) {
    return Optional.ofNullable(certificateGroups.put(group.getCertificateGroupId(), group));
  }

  /**
   * Remove a {@link CertificateGroup} from this manager.
   *
   * <p>The removed group is returned without being closed.
   *
   * @param certificateGroupId the ID of the group to remove.
   * @return the removed group, or empty if the ID was not registered.
   */
  public Optional<CertificateGroup> removeCertificateGroup(NodeId certificateGroupId) {
    return Optional.ofNullable(certificateGroups.remove(certificateGroupId));
  }

  @Override
  public Optional<KeyPair> getKeyPair(ByteString thumbprint) {
    return firstMatchingEntry(thumbprint)
        .flatMap(match -> match.group.getKeyPair(match.entry.certificateTypeId));
  }

  @Override
  public Optional<X509Certificate> getCertificate(ByteString thumbprint) {
    return firstMatchingEntry(thumbprint).map(match -> match.entry.certificateChain[0]);
  }

  @Override
  public Optional<X509Certificate[]> getCertificateChain(ByteString thumbprint) {
    return firstMatchingEntry(thumbprint).map(match -> match.entry.certificateChain);
  }

  @Override
  public Optional<CertificateGroup> getCertificateGroup(ByteString thumbprint) {
    return firstMatchingEntry(thumbprint).map(MatchedEntry::group);
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

  private Optional<MatchedEntry> firstMatchingEntry(ByteString thumbprint) {
    return certificateGroups.values().stream()
        .flatMap(
            group ->
                group.getCertificateEntries().stream().map(entry -> new MatchedEntry(group, entry)))
        .filter(
            match -> {
              try {
                return CertificateUtil.thumbprint(match.entry.certificateChain[0])
                    .equals(thumbprint);
              } catch (UaException e) {
                return false;
              }
            })
        .findFirst();
  }

  private record MatchedEntry(CertificateGroup group, Entry entry) {}
}
