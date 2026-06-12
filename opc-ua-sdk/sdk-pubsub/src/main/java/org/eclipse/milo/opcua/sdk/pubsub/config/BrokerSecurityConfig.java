/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.config;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;
import org.jspecify.annotations.Nullable;

/**
 * Broker session security settings for an {@link MqttConnectionConfig}: TLS, credentials, and
 * certificate material used when connecting to the broker.
 *
 * <p>These settings configure the broker client itself and are not part of the Part 14
 * configuration model; they do not round-trip through {@code PubSubConfiguration2DataType}. Broker
 * transport modules (e.g. {@code sdk-pubsub-mqtt}) apply them when opening the broker session: TLS
 * enablement and certificate material, and username/password credentials.
 */
public final class BrokerSecurityConfig {

  private final boolean tls;
  private final @Nullable String username;
  private final char @Nullable [] password;
  private final @Nullable Path caCertificate;
  private final @Nullable Path clientCertificate;
  private final @Nullable Path clientKey;
  private final boolean allowUntrustedCertificates;

  private BrokerSecurityConfig(Builder builder) {
    this.tls = builder.tls;
    this.username = builder.username;
    this.password = builder.password != null ? builder.password.clone() : null;
    this.caCertificate = builder.caCertificate;
    this.clientCertificate = builder.clientCertificate;
    this.clientKey = builder.clientKey;
    this.allowUntrustedCertificates = builder.allowUntrustedCertificates;
  }

  /**
   * Check if TLS is used for the broker connection.
   *
   * @return {@code true} if TLS is enabled.
   */
  public boolean isTls() {
    return tls;
  }

  /**
   * Get the username used to authenticate with the broker.
   *
   * @return the username, or {@code null} if not configured.
   */
  public @Nullable String getUsername() {
    return username;
  }

  /**
   * Get the password used to authenticate with the broker.
   *
   * @return a copy of the password, or {@code null} if not configured.
   */
  public char @Nullable [] getPassword() {
    return password != null ? password.clone() : null;
  }

  /**
   * Get the path to the CA certificate used to verify the broker's certificate.
   *
   * @return the CA certificate path, or {@code null} if not configured.
   */
  public @Nullable Path getCaCertificate() {
    return caCertificate;
  }

  /**
   * Get the path to the client certificate presented to the broker.
   *
   * @return the client certificate path, or {@code null} if not configured.
   */
  public @Nullable Path getClientCertificate() {
    return clientCertificate;
  }

  /**
   * Get the path to the private key for the client certificate.
   *
   * @return the client key path, or {@code null} if not configured.
   */
  public @Nullable Path getClientKey() {
    return clientKey;
  }

  /**
   * Check if untrusted broker certificates are accepted.
   *
   * @return {@code true} if untrusted broker certificates are accepted.
   */
  public boolean isAllowUntrustedCertificates() {
    return allowUntrustedCertificates;
  }

  /**
   * Create a new {@link Builder} initialized with the values of this instance.
   *
   * @return a new {@link Builder}.
   */
  public Builder toBuilder() {
    Builder builder = new Builder();
    builder.tls = tls;
    builder.username = username;
    builder.password = password != null ? password.clone() : null;
    builder.caCertificate = caCertificate;
    builder.clientCertificate = clientCertificate;
    builder.clientKey = clientKey;
    builder.allowUntrustedCertificates = allowUntrustedCertificates;
    return builder;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof BrokerSecurityConfig that)) {
      return false;
    }
    return tls == that.tls
        && Objects.equals(username, that.username)
        && Arrays.equals(password, that.password)
        && Objects.equals(caCertificate, that.caCertificate)
        && Objects.equals(clientCertificate, that.clientCertificate)
        && Objects.equals(clientKey, that.clientKey)
        && allowUntrustedCertificates == that.allowUntrustedCertificates;
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        tls,
        username,
        Arrays.hashCode(password),
        caCertificate,
        clientCertificate,
        clientKey,
        allowUntrustedCertificates);
  }

  /**
   * Create a new {@link Builder} with default values.
   *
   * @return a new {@link Builder}.
   */
  public static Builder builder() {
    return new Builder();
  }

  /** A builder of {@link BrokerSecurityConfig} instances. */
  public static final class Builder {

    private boolean tls = false;
    private @Nullable String username;
    private char @Nullable [] password;
    private @Nullable Path caCertificate;
    private @Nullable Path clientCertificate;
    private @Nullable Path clientKey;
    private boolean allowUntrustedCertificates = false;

    private Builder() {}

    /**
     * Set whether TLS is used for the broker connection.
     *
     * @param enabled {@code true} to enable TLS; defaults to {@code false}.
     * @return this {@link Builder}.
     */
    public Builder tls(boolean enabled) {
      this.tls = enabled;
      return this;
    }

    /**
     * Set the username used to authenticate with the broker.
     *
     * @param username the username.
     * @return this {@link Builder}.
     */
    public Builder username(String username) {
      this.username = username;
      return this;
    }

    /**
     * Set the password used to authenticate with the broker.
     *
     * @param password the password; the array is copied.
     * @return this {@link Builder}.
     */
    public Builder password(char[] password) {
      this.password = password.clone();
      return this;
    }

    /**
     * Set the path to the CA certificate used to verify the broker's certificate.
     *
     * @param path the CA certificate path.
     * @return this {@link Builder}.
     */
    public Builder caCertificate(Path path) {
      this.caCertificate = path;
      return this;
    }

    /**
     * Set the path to the client certificate presented to the broker.
     *
     * @param path the client certificate path.
     * @return this {@link Builder}.
     */
    public Builder clientCertificate(Path path) {
      this.clientCertificate = path;
      return this;
    }

    /**
     * Set the path to the private key for the client certificate.
     *
     * @param path the client key path.
     * @return this {@link Builder}.
     */
    public Builder clientKey(Path path) {
      this.clientKey = path;
      return this;
    }

    /**
     * Set whether untrusted broker certificates are accepted.
     *
     * @param allow {@code true} to accept untrusted broker certificates; defaults to {@code false}.
     * @return this {@link Builder}.
     */
    public Builder allowUntrustedCertificates(boolean allow) {
      this.allowUntrustedCertificates = allow;
      return this;
    }

    /**
     * Build a new {@link BrokerSecurityConfig} from the values configured on this builder.
     *
     * @return a new {@link BrokerSecurityConfig}.
     */
    public BrokerSecurityConfig build() {
      return new BrokerSecurityConfig(this);
    }
  }
}
