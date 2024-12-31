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
   * Get the {@link CertificateGroup}s managed by this {@link CertificateManager}.
   *
   * @return the {@link CertificateGroup}s managed by this {@link CertificateManager}.
   */
  List<CertificateGroup> getCertificateGroups();

  /**
   * Get the Server's {@link CertificateQuarantine}.
   *
   * @return the Server's {@link CertificateQuarantine}.
   */
  CertificateQuarantine getCertificateQuarantine();

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
