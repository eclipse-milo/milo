/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import static java.util.Objects.requireNonNull;

import java.security.cert.X509Certificate;
import java.util.Optional;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.security.CertificateManager;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Describes the certificate identity a server endpoint should advertise.
 *
 * <p>Use this with {@link EndpointConfig.Builder#setEndpointCertificateConfig} when an endpoint
 * should select its advertised certificate from the server {@link CertificateManager}. Endpoints
 * that use {@link EndpointConfig.Builder#setCertificate(X509Certificate)} continue to advertise
 * that fixed certificate directly.
 */
@NullMarked
public final class EndpointCertificateConfig {

  private final NodeId certificateGroupId;
  private final @Nullable NodeId certificateTypeId;

  private EndpointCertificateConfig(NodeId certificateGroupId, @Nullable NodeId certificateTypeId) {

    this.certificateGroupId = certificateGroupId;
    this.certificateTypeId = certificateTypeId;
  }

  /**
   * Get the certificate group to search for endpoint certificates.
   *
   * @return the certificate group ID.
   */
  public NodeId getCertificateGroupId() {
    return certificateGroupId;
  }

  /**
   * Get the requested certificate type.
   *
   * @return the certificate type ID, or empty when the endpoint security policy should choose.
   */
  public Optional<NodeId> getCertificateTypeId() {
    return Optional.ofNullable(certificateTypeId);
  }

  /**
   * Copy this config into a new builder.
   *
   * @return a builder initialized with this config's values.
   */
  public Builder copy() {
    return new Builder()
        .setCertificateGroupId(certificateGroupId)
        .setCertificateTypeId(certificateTypeId);
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof EndpointCertificateConfig that)) {
      return false;
    }
    return certificateGroupId.equals(that.certificateGroupId)
        && java.util.Objects.equals(certificateTypeId, that.certificateTypeId);
  }

  @Override
  public int hashCode() {
    return java.util.Objects.hash(certificateGroupId, certificateTypeId);
  }

  @Override
  public String toString() {
    return "EndpointCertificateConfig{"
        + "certificateGroupId="
        + certificateGroupId
        + ", certificateTypeId="
        + certificateTypeId
        + '}';
  }

  /**
   * Create a new endpoint certificate config builder.
   *
   * @return a new builder.
   */
  public static Builder newBuilder() {
    return new Builder();
  }

  /** Builder for {@link EndpointCertificateConfig}. */
  public static final class Builder {

    private NodeId certificateGroupId =
        NodeIds.ServerConfiguration_CertificateGroups_DefaultApplicationGroup;
    private @Nullable NodeId certificateTypeId;

    private Builder() {}

    /**
     * Set the certificate group to search.
     *
     * @param certificateGroupId the certificate group ID.
     * @return this builder.
     */
    public Builder setCertificateGroupId(NodeId certificateGroupId) {
      this.certificateGroupId = requireNonNull(certificateGroupId, "certificateGroupId");
      return this;
    }

    /**
     * Set the requested certificate type.
     *
     * @param certificateTypeId the certificate type ID, or {@code null} to let the endpoint
     *     security policy choose.
     * @return this builder.
     */
    public Builder setCertificateTypeId(@Nullable NodeId certificateTypeId) {
      this.certificateTypeId = certificateTypeId;
      return this;
    }

    /**
     * Build the endpoint certificate config.
     *
     * @return the endpoint certificate config.
     */
    public EndpointCertificateConfig build() {
      return new EndpointCertificateConfig(certificateGroupId, certificateTypeId);
    }
  }
}
