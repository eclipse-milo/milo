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
import java.util.List;
import java.util.Optional;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

/**
 * A server's registry of {@link CertificateGroup}s keyed by the {@link NodeId} of their {@code
 * CertificateGroupType} node.
 *
 * <p>Endpoint configuration, Push management, and address-space code all name groups by NodeId; the
 * manager resolves those ids to groups and groups back to ids. Thumbprint lookups resolve the local
 * key material that a SecureChannel or session handshake identifies by certificate thumbprint.
 *
 * <p>{@link #getCertificateGroups()} returns groups in registration order. That order is the
 * precedence used when several groups could satisfy the same request, so it is a configuration
 * decision made by the application when it registers its groups.
 */
public interface CertificateManager {

  /**
   * Get the {@link KeyPair} belonging to the certificate identified by {@code thumbprint}.
   *
   * <p>{@code thumbprint} is a SHA1 hash of the encoded certificate.
   *
   * @param thumbprint the thumbprint of the certificate.
   * @return the {@link KeyPair} belonging to the certificate identified by {@code thumbprint}.
   */
  Optional<KeyPair> getKeyPair(ByteString thumbprint);

  /**
   * Get the {@link X509Certificate} identified by {@code thumbprint}.
   *
   * <p>{@code thumbprint} is a SHA1 hash of the encoded certificate.
   *
   * @param thumbprint the thumbprint of the certificate.
   * @return the {@link X509Certificate} identified by {@code thumbprint}.
   */
  Optional<X509Certificate> getCertificate(ByteString thumbprint);

  /**
   * Get the {@link X509Certificate} identified by {@code thumbprint} as well as any certificates in
   * its chain.
   *
   * @param thumbprint the thumbprint of the certificate.
   * @return the {@link X509Certificate} identified by {@code thumbprint} as well as any
   *     certificates in its chain.
   */
  Optional<X509Certificate[]> getCertificateChain(ByteString thumbprint);

  /**
   * Get the {@link CertificateGroup} containing the {@link X509Certificate} identified by {@code
   * thumbprint}.
   *
   * @param thumbprint the thumbprint of the certificate.
   * @return the {@link CertificateGroup} containing the {@link X509Certificate} identified by
   *     {@code thumbprint}.
   */
  Optional<CertificateGroup> getCertificateGroup(ByteString thumbprint);

  /**
   * Get the {@link CertificateGroup} identified by {@code certificateGroupId}.
   *
   * @param certificateGroupId the {@link NodeId} identifying the {@link CertificateGroup}.
   * @return the {@link CertificateGroup} identified by {@code certificateGroupId}.
   */
  Optional<CertificateGroup> getCertificateGroup(NodeId certificateGroupId);

  /**
   * Get the {@link NodeId} under which {@code certificateGroup} is registered.
   *
   * @param certificateGroup the {@link CertificateGroup} to look up.
   * @return the {@link NodeId} under which {@code certificateGroup} is registered, or empty if it
   *     is not registered with this manager.
   */
  Optional<NodeId> getCertificateGroupId(CertificateGroup certificateGroup);

  /**
   * Get the {@link CertificateGroup}s managed by this {@link CertificateManager}, in registration
   * order.
   *
   * <p>Registration order is the precedence between groups when more than one could satisfy a
   * request.
   *
   * @return the {@link CertificateGroup}s managed by this {@link CertificateManager}.
   */
  List<CertificateGroup> getCertificateGroups();

  /**
   * Get the usable certificate identities managed by this {@link CertificateManager}.
   *
   * <p>Identities are listed group by group in {@link #getCertificateGroups()} order, and within a
   * group in that group's {@link CertificateGroup#getCertificateIdentities()} order.
   *
   * @return the usable certificate identities managed by this {@link CertificateManager}.
   */
  default List<CertificateIdentity> getCertificateIdentities() {
    return getCertificateGroups().stream()
        .flatMap(group -> group.getCertificateIdentities().stream())
        .toList();
  }

  /**
   * Get the DefaultApplicationGroup {@link CertificateGroup}, if it's configured.
   *
   * <p>Servers are required to support the DefaultApplicationGroup CertificateGroup.
   *
   * @return the DefaultApplicationGroup {@link CertificateGroup}, if it's configured.
   */
  default Optional<CertificateGroup> getDefaultApplicationGroup() {
    return getCertificateGroup(
        NodeIds.ServerConfiguration_CertificateGroups_DefaultApplicationGroup);
  }

  /**
   * Get the DefaultUserTokenGroup {@link CertificateGroup}, if it's configured.
   *
   * <p>Support for the DefaultUserTokenGroup CertificateGroup is optional.
   *
   * @return the DefaultUserTokenGroup {@link CertificateGroup}, if it's configured.
   */
  default Optional<CertificateGroup> getDefaultUserTokenGroup() {
    return getCertificateGroup(NodeIds.ServerConfiguration_CertificateGroups_DefaultUserTokenGroup);
  }

  /**
   * Get the DefaultHttpsGroup {@link CertificateGroup}, if it's configured.
   *
   * <p>Support for the DefaultHttpsGroup CertificateGroup is optional.
   *
   * @return the DefaultHttpsGroup {@link CertificateGroup}, if it's configured.
   */
  default Optional<CertificateGroup> getDefaultHttpsGroup() {
    return getCertificateGroup(NodeIds.ServerConfiguration_CertificateGroups_DefaultHttpsGroup);
  }
}
