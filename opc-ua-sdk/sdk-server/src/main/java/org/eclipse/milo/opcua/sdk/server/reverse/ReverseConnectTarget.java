/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.reverse;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import com.google.common.base.MoreObjects;
import java.util.Objects;
import java.util.UUID;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;
import org.jspecify.annotations.Nullable;

/**
 * Immutable configuration for one server-side Reverse Connect target.
 *
 * <p>A target describes the client reverse listener the server should dial, the local server
 * endpoint URL advertised in the OPC UA {@code ReverseHello}, and the timing used for repeated
 * connection attempts. The target id is the stable lifecycle key used by {@link OpcUaServer} when
 * updating snapshots, listeners, and runtime control handles.
 *
 * <pre>{@code
 * ReverseConnectTarget target =
 *     ReverseConnectTarget.builder()
 *         .setClientListenerUrl("opc.tcp://client.example.com:48060")
 *         .setEndpointUrl("opc.tcp://server.example.com:12686/milo")
 *         .setRegistrationPeriod(uint(1_000))
 *         .setConnectTimeout(uint(5_000))
 *         .build();
 * }</pre>
 */
public final class ReverseConnectTarget {

  private final UUID id;
  private final String clientListenerUrl;
  private final String endpointUrl;
  private final UInteger registrationPeriod;
  private final UInteger connectTimeout;
  private final boolean enabled;
  private final boolean paused;
  private final ReverseConnectRetryPolicy retryPolicy;

  private ReverseConnectTarget(
      UUID id,
      String clientListenerUrl,
      String endpointUrl,
      UInteger registrationPeriod,
      UInteger connectTimeout,
      boolean enabled,
      boolean paused,
      ReverseConnectRetryPolicy retryPolicy) {

    this.id = id;
    this.clientListenerUrl = clientListenerUrl;
    this.endpointUrl = endpointUrl;
    this.registrationPeriod = registrationPeriod;
    this.connectTimeout = connectTimeout;
    this.enabled = enabled;
    this.paused = paused;
    this.retryPolicy = retryPolicy;
  }

  /**
   * Get the stable lifecycle id for this target.
   *
   * @return the stable lifecycle id for this target.
   */
  public UUID getId() {
    return id;
  }

  /**
   * Get the {@code opc.tcp} URL of the client reverse listener the server should dial.
   *
   * @return the client reverse-listener URL.
   */
  public String getClientListenerUrl() {
    return clientListenerUrl;
  }

  /**
   * Get the local {@code opc.tcp} endpoint URL advertised in {@code ReverseHello}.
   *
   * @return the server endpoint URL advertised to the client.
   */
  public String getEndpointUrl() {
    return endpointUrl;
  }

  /**
   * Get the target registration period.
   *
   * <p>The registration period is used as the default retry delay by {@link
   * ReverseConnectRetryPolicy#registrationPeriod()}.
   *
   * @return the target registration period, in milliseconds.
   */
  public UInteger getRegistrationPeriod() {
    return registrationPeriod;
  }

  /**
   * Get the TCP connection timeout for each outbound reverse-connect attempt.
   *
   * @return the TCP connection timeout, in milliseconds.
   */
  public UInteger getConnectTimeout() {
    return connectTimeout;
  }

  /**
   * Get whether this target is enabled.
   *
   * @return true if the target may be scheduled when not paused.
   */
  public boolean isEnabled() {
    return enabled;
  }

  /**
   * Get whether this target starts paused.
   *
   * @return true if the target starts registered but unscheduled.
   */
  public boolean isPaused() {
    return paused;
  }

  /**
   * Get the retry policy used after failed reverse-connect attempts.
   *
   * @return the retry policy used after failed reverse-connect attempts.
   */
  public ReverseConnectRetryPolicy getRetryPolicy() {
    return retryPolicy;
  }

  @Override
  public boolean equals(@Nullable Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ReverseConnectTarget that = (ReverseConnectTarget) o;
    return enabled == that.enabled
        && paused == that.paused
        && Objects.equals(id, that.id)
        && Objects.equals(clientListenerUrl, that.clientListenerUrl)
        && Objects.equals(endpointUrl, that.endpointUrl)
        && Objects.equals(registrationPeriod, that.registrationPeriod)
        && Objects.equals(connectTimeout, that.connectTimeout)
        && Objects.equals(retryPolicy, that.retryPolicy);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        id,
        clientListenerUrl,
        endpointUrl,
        registrationPeriod,
        connectTimeout,
        enabled,
        paused,
        retryPolicy);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", id)
        .add("clientListenerUrl", clientListenerUrl)
        .add("endpointUrl", endpointUrl)
        .add("registrationPeriod", registrationPeriod)
        .add("connectTimeout", connectTimeout)
        .add("enabled", enabled)
        .add("paused", paused)
        .add("retryPolicy", retryPolicy)
        .toString();
  }

  /**
   * Create a builder for a server-side Reverse Connect target.
   *
   * @return a new target builder.
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builder for {@link ReverseConnectTarget}. */
  public static final class Builder {

    private UUID id = UUID.randomUUID();
    private @Nullable String clientListenerUrl;
    private @Nullable String endpointUrl;
    private UInteger registrationPeriod = uint(30_000);
    private UInteger connectTimeout = uint(5_000);
    private boolean enabled = true;
    private boolean paused = false;
    private ReverseConnectRetryPolicy retryPolicy = ReverseConnectRetryPolicy.registrationPeriod();

    /**
     * Set the stable lifecycle id for this target.
     *
     * <p>The id is used to de-duplicate targets and to address the target at runtime. If omitted, a
     * random id is generated.
     *
     * @param id the target id.
     * @return this builder.
     */
    public Builder setId(UUID id) {
      this.id = id;
      return this;
    }

    /**
     * Set the {@code opc.tcp} URL of the client reverse listener the server should dial.
     *
     * @param clientListenerUrl the client reverse-listener URL.
     * @return this builder.
     */
    public Builder setClientListenerUrl(String clientListenerUrl) {
      this.clientListenerUrl = clientListenerUrl;
      return this;
    }

    /**
     * Set the local {@code opc.tcp} endpoint URL advertised in {@code ReverseHello}.
     *
     * <p>The URL must match one of the server's configured {@code opc.tcp} endpoints by full URL or
     * by endpoint path.
     *
     * @param endpointUrl the endpoint URL advertised to the client.
     * @return this builder.
     */
    public Builder setEndpointUrl(String endpointUrl) {
      this.endpointUrl = endpointUrl;
      return this;
    }

    /**
     * Set the target registration period in milliseconds.
     *
     * <p>The registration period is used as the default retry delay by {@link
     * ReverseConnectRetryPolicy#registrationPeriod()}.
     *
     * @param registrationPeriod the target registration period.
     * @return this builder.
     */
    public Builder setRegistrationPeriod(UInteger registrationPeriod) {
      this.registrationPeriod = registrationPeriod;
      return this;
    }

    /**
     * Set the TCP connection timeout for each outbound reverse-connect attempt.
     *
     * @param connectTimeout the TCP connection timeout, in milliseconds.
     * @return this builder.
     */
    public Builder setConnectTimeout(UInteger connectTimeout) {
      this.connectTimeout = connectTimeout;
      return this;
    }

    /**
     * Set whether this target is enabled.
     *
     * <p>Disabled targets remain registered and observable, but they are not scheduled until
     * replaced by a future target configuration.
     *
     * @param enabled true if the target may be scheduled when not paused.
     * @return this builder.
     */
    public Builder setEnabled(boolean enabled) {
      this.enabled = enabled;
      return this;
    }

    /**
     * Set whether this target starts paused.
     *
     * <p>Paused targets remain registered and observable. They can be scheduled later through a
     * {@link ReverseConnectTargetHandle}.
     *
     * @param paused true if the target should start registered but unscheduled.
     * @return this builder.
     */
    public Builder setPaused(boolean paused) {
      this.paused = paused;
      return this;
    }

    /**
     * Set the retry policy used after failed reverse-connect attempts and after a reverse-opened
     * channel closes.
     *
     * @param retryPolicy the retry policy.
     * @return this builder.
     */
    public Builder setRetryPolicy(ReverseConnectRetryPolicy retryPolicy) {
      this.retryPolicy = retryPolicy;
      return this;
    }

    /**
     * Build the immutable target configuration.
     *
     * @return the immutable target configuration.
     * @throws IllegalStateException if a required URL has not been configured.
     * @throws IllegalArgumentException if URL or timing values are invalid.
     */
    public ReverseConnectTarget build() {
      String clientListenerUrl = this.clientListenerUrl;
      String endpointUrl = this.endpointUrl;

      if (clientListenerUrl == null) {
        throw new IllegalStateException("clientListenerUrl must be configured");
      }
      if (endpointUrl == null) {
        throw new IllegalStateException("endpointUrl must be configured");
      }

      validateOpcTcpUrl(clientListenerUrl, "clientListenerUrl");
      validateOpcTcpUrl(endpointUrl, "endpointUrl");

      if (registrationPeriod.longValue() <= 0) {
        throw new IllegalArgumentException("registrationPeriod must be greater than 0");
      }
      if (connectTimeout.longValue() <= 0 || connectTimeout.longValue() > Integer.MAX_VALUE) {
        throw new IllegalArgumentException(
            "connectTimeout must be greater than 0 and less than Integer.MAX_VALUE");
      }

      return new ReverseConnectTarget(
          id,
          clientListenerUrl,
          endpointUrl,
          registrationPeriod,
          connectTimeout,
          enabled,
          paused,
          retryPolicy);
    }

    private static void validateOpcTcpUrl(String url, String fieldName) {
      String scheme = EndpointUtil.getScheme(url);
      String host = EndpointUtil.getHost(url);

      if (!"opc.tcp".equals(scheme)) {
        throw new IllegalArgumentException(fieldName + " must use opc.tcp: " + url);
      }
      if (host == null || host.isBlank()) {
        throw new IllegalArgumentException(fieldName + " has no host: " + url);
      }
    }
  }
}
