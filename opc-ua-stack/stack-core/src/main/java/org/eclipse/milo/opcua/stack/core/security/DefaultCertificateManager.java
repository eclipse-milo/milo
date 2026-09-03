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
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.CertificateGroup.Entry;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;

/**
 * A thread-safe, mutable registry of application-owned {@link CertificateGroup}s keyed by the
 * {@link NodeId} of their {@code CertificateGroupType} node.
 *
 * <p>Groups are kept in registration order, which is the precedence reported by {@link
 * #getCertificateGroups()}. Registering a group under an id that is already registered replaces the
 * previous group in place without changing its position. A group instance may be registered under
 * one id only, and {@link #getCertificateGroupId} finds a group only through that same instance.
 *
 * <p>Removing or replacing a group does not initialize or close it; the application remains
 * responsible for the lifecycle of every group and its resources. Registry changes do not wait for
 * aggregate lookups already in progress, and each lookup remains bound to the group instance it
 * observed.
 *
 * <p>Example, a server with one group registered as its DefaultApplicationGroup:
 *
 * <pre>{@code
 * var group = new DefaultCertificateGroup(trustListManager, certificateStore, quarantine, validator);
 * certificateFactory.createMissingCertificates(group);
 *
 * var certificateManager = new DefaultCertificateManager(group);
 * }</pre>
 */
public class DefaultCertificateManager implements CertificateManager {

  private final Object registrationLock = new Object();
  private volatile List<Registration> registrations = List.of();

  /** Create an empty manager. */
  public DefaultCertificateManager() {}

  /**
   * Create a manager with {@code defaultApplicationGroup} registered under {@link
   * NodeIds#ServerConfiguration_CertificateGroups_DefaultApplicationGroup}.
   *
   * @param defaultApplicationGroup the group to register as the DefaultApplicationGroup.
   */
  public DefaultCertificateManager(CertificateGroup defaultApplicationGroup) {
    addCertificateGroup(
        NodeIds.ServerConfiguration_CertificateGroups_DefaultApplicationGroup,
        defaultApplicationGroup);
  }

  /**
   * Register a {@link CertificateGroup} under {@code certificateGroupId}.
   *
   * <p>If a group is already registered under {@code certificateGroupId}, it is replaced in place,
   * keeping its position in {@link #getCertificateGroups()}, and returned without being closed.
   * Otherwise the group is appended after every group registered so far.
   *
   * @param certificateGroupId the {@link NodeId} of the group's {@code CertificateGroupType} node.
   * @param certificateGroup the application-owned group to register.
   * @return the replaced group, or empty if {@code certificateGroupId} was not registered.
   * @throws IllegalArgumentException if {@code certificateGroup} is already registered under a
   *     different id.
   */
  public Optional<CertificateGroup> addCertificateGroup(
      NodeId certificateGroupId, CertificateGroup certificateGroup) {

    synchronized (registrationLock) {
      var updated = new ArrayList<Registration>(registrations.size() + 1);
      CertificateGroup replaced = null;
      boolean found = false;

      for (Registration registration : registrations) {
        if (registration.certificateGroupId.equals(certificateGroupId)) {
          replaced = registration.certificateGroup;
          found = true;
          updated.add(new Registration(certificateGroupId, certificateGroup));
        } else {
          if (registration.certificateGroup == certificateGroup) {
            throw new IllegalArgumentException(
                "certificate group is already registered under "
                    + registration.certificateGroupId.toParseableString());
          }
          updated.add(registration);
        }
      }

      if (!found) {
        updated.add(new Registration(certificateGroupId, certificateGroup));
      }

      registrations = List.copyOf(updated);

      return Optional.ofNullable(replaced);
    }
  }

  /**
   * Remove the {@link CertificateGroup} registered under {@code certificateGroupId}.
   *
   * <p>The removed group is returned without being closed.
   *
   * @param certificateGroupId the id of the group to remove.
   * @return the removed group, or empty if the id was not registered.
   */
  public Optional<CertificateGroup> removeCertificateGroup(NodeId certificateGroupId) {
    synchronized (registrationLock) {
      CertificateGroup removed = null;
      var updated = new ArrayList<Registration>(registrations.size());

      for (Registration registration : registrations) {
        if (registration.certificateGroupId.equals(certificateGroupId)) {
          removed = registration.certificateGroup;
        } else {
          updated.add(registration);
        }
      }

      registrations = List.copyOf(updated);

      return Optional.ofNullable(removed);
    }
  }

  /**
   * Remove {@code certificateGroup} from this manager.
   *
   * <p>The group is not closed.
   *
   * @param certificateGroup the group to remove.
   * @return {@code true} if the group was registered and has been removed.
   */
  public boolean removeCertificateGroup(CertificateGroup certificateGroup) {
    synchronized (registrationLock) {
      Optional<NodeId> certificateGroupId = getCertificateGroupId(certificateGroup);

      return certificateGroupId.isPresent()
          && removeCertificateGroup(certificateGroupId.get()).isPresent();
    }
  }

  @Override
  public Optional<KeyPair> getKeyPair(ByteString thumbprint) {
    return firstMatchingEntry(thumbprint)
        .flatMap(match -> match.group.getKeyPair(match.entry.certificateTypeId()));
  }

  @Override
  public Optional<X509Certificate> getCertificate(ByteString thumbprint) {
    return firstMatchingEntry(thumbprint).map(match -> match.entry.certificateChain()[0]);
  }

  @Override
  public Optional<X509Certificate[]> getCertificateChain(ByteString thumbprint) {
    return firstMatchingEntry(thumbprint).map(match -> match.entry.certificateChain());
  }

  @Override
  public Optional<CertificateGroup> getCertificateGroup(ByteString thumbprint) {
    return firstMatchingEntry(thumbprint).map(MatchedEntry::group);
  }

  @Override
  public Optional<CertificateGroup> getCertificateGroup(NodeId certificateGroupId) {
    for (Registration registration : registrations) {
      if (registration.certificateGroupId.equals(certificateGroupId)) {
        return Optional.of(registration.certificateGroup);
      }
    }
    return Optional.empty();
  }

  @Override
  public Optional<NodeId> getCertificateGroupId(CertificateGroup certificateGroup) {
    for (Registration registration : registrations) {
      if (registration.certificateGroup == certificateGroup) {
        return Optional.of(registration.certificateGroupId);
      }
    }
    return Optional.empty();
  }

  @Override
  public List<CertificateGroup> getCertificateGroups() {
    return registrations.stream().map(Registration::certificateGroup).toList();
  }

  private Optional<MatchedEntry> firstMatchingEntry(ByteString thumbprint) {
    return registrations.stream()
        .map(Registration::certificateGroup)
        .flatMap(
            group ->
                group.getCertificateEntries().stream().map(entry -> new MatchedEntry(group, entry)))
        .filter(
            match -> {
              try {
                return CertificateUtil.thumbprint(match.entry.certificateChain()[0])
                    .equals(thumbprint);
              } catch (UaException e) {
                return false;
              }
            })
        .findFirst();
  }

  private record Registration(NodeId certificateGroupId, CertificateGroup certificateGroup) {}

  private record MatchedEntry(CertificateGroup group, Entry entry) {}
}
