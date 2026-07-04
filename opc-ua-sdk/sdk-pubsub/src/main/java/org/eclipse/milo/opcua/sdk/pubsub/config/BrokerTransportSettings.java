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

import java.time.Duration;
import java.util.Objects;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrokerTransportQualityOfService;
import org.jspecify.annotations.Nullable;

/**
 * Broker (e.g. MQTT) transport settings, accepted at the writer group, dataset writer, and dataset
 * reader levels via the {@code brokerTransport(...)} builder methods.
 *
 * <p>On broker connections these settings drive the runtime: queue (topic) names are resolved per
 * {@link org.eclipse.milo.opcua.sdk.pubsub.transport.BrokerTopics} (a configured queue name always
 * wins over the derived Part 14 §7.3.4.7 topic), delivery guarantees per {@link
 * org.eclipse.milo.opcua.sdk.pubsub.transport.BrokerQualityOfService}, and {@code
 * metaDataQueueName}/{@code metaDataUpdateTime} configure the retained DataSetMetaData publication.
 * On UDP connections they are inert config, kept for round-trip fidelity with the Part 14
 * configuration model.
 *
 * <p>Settings at the writer level override those of the containing group. Note that a writer-level
 * instance, when present, is authoritative for <i>all</i> of its values including defaults: see
 * {@link #getMetaDataUpdateTime()} and {@link #getRequestedDeliveryGuarantee()} for the
 * consequences.
 */
public final class BrokerTransportSettings {

  private final @Nullable String queueName;
  private final @Nullable String metaDataQueueName;
  private final Duration metaDataUpdateTime;
  private final @Nullable String resourceUri;
  private final @Nullable String authenticationProfileUri;
  private final BrokerTransportQualityOfService requestedDeliveryGuarantee;

  private BrokerTransportSettings(Builder builder) {
    this.queueName = builder.queueName;
    this.metaDataQueueName = builder.metaDataQueueName;
    this.metaDataUpdateTime = builder.metaDataUpdateTime;
    this.resourceUri = builder.resourceUri;
    this.authenticationProfileUri = builder.authenticationProfileUri;
    this.requestedDeliveryGuarantee = builder.requestedDeliveryGuarantee;
  }

  /**
   * Get the broker queue (e.g. MQTT topic) that NetworkMessages are sent to or received from.
   *
   * @return the queue name, or {@code null} if not configured at this level.
   */
  public @Nullable String getQueueName() {
    return queueName;
  }

  /**
   * Get the broker queue that DataSetMetaData messages are sent to or received from.
   *
   * @return the metadata queue name, or {@code null} if not configured at this level.
   */
  public @Nullable String getMetaDataQueueName() {
    return metaDataQueueName;
  }

  /**
   * Get the interval at which DataSetMetaData is sent to the metadata queue.
   *
   * <p>A writer-level {@link BrokerTransportSettings}, when present, is authoritative for this
   * value <i>including its default of zero</i> (the value cannot distinguish unset from 0):
   * attaching broker settings to a writer — e.g. only to configure a queue name — disables any
   * group-level periodic metadata publication for that writer unless the writer-level settings also
   * carry an update time.
   *
   * @return the metadata update interval; {@link Duration#ZERO} means metadata is not sent
   *     cyclically.
   */
  public Duration getMetaDataUpdateTime() {
    return metaDataUpdateTime;
  }

  /**
   * Get the resource URI used to look up broker connection settings.
   *
   * @return the resource URI, or {@code null} if not configured.
   */
  public @Nullable String getResourceUri() {
    return resourceUri;
  }

  /**
   * Get the authentication profile URI used to look up broker credentials.
   *
   * @return the authentication profile URI, or {@code null} if not configured.
   */
  public @Nullable String getAuthenticationProfileUri() {
    return authenticationProfileUri;
  }

  /**
   * Get the delivery guarantee (quality of service) requested from the broker.
   *
   * <p>For data NetworkMessages a writer-level value takes effect only when the writer also
   * overrides {@link #getQueueName() queueName} (Part 14 §6.4.2.5.4: a DataSetWriter-level delivery
   * guarantee is only valid alongside a QueueName override); without a queue override the writer
   * publishes into the group's NetworkMessages and the group-level value applies. A writer-level
   * value without a queueName override is therefore rejected with {@code Bad_ConfigurationError}
   * for enabled writers on broker connections when the service starts or reconfigures; on UDP
   * connections, where these settings are inert config, the combination is tolerated for round-trip
   * fidelity. The writer's DataSetMetaData messages always honor a writer-level value.
   *
   * @return the requested {@link BrokerTransportQualityOfService}.
   */
  public BrokerTransportQualityOfService getRequestedDeliveryGuarantee() {
    return requestedDeliveryGuarantee;
  }

  /**
   * Create a new {@link Builder} initialized with the values of this instance.
   *
   * @return a new {@link Builder}.
   */
  public Builder toBuilder() {
    Builder builder = new Builder();
    builder.queueName = queueName;
    builder.metaDataQueueName = metaDataQueueName;
    builder.metaDataUpdateTime = metaDataUpdateTime;
    builder.resourceUri = resourceUri;
    builder.authenticationProfileUri = authenticationProfileUri;
    builder.requestedDeliveryGuarantee = requestedDeliveryGuarantee;
    return builder;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof BrokerTransportSettings that)) {
      return false;
    }
    return Objects.equals(queueName, that.queueName)
        && Objects.equals(metaDataQueueName, that.metaDataQueueName)
        && metaDataUpdateTime.equals(that.metaDataUpdateTime)
        && Objects.equals(resourceUri, that.resourceUri)
        && Objects.equals(authenticationProfileUri, that.authenticationProfileUri)
        && requestedDeliveryGuarantee == that.requestedDeliveryGuarantee;
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        queueName,
        metaDataQueueName,
        metaDataUpdateTime,
        resourceUri,
        authenticationProfileUri,
        requestedDeliveryGuarantee);
  }

  /**
   * Create a new {@link Builder} with default values.
   *
   * @return a new {@link Builder}.
   */
  public static Builder builder() {
    return new Builder();
  }

  /** A builder of {@link BrokerTransportSettings} instances. */
  public static final class Builder {

    private @Nullable String queueName;
    private @Nullable String metaDataQueueName;
    private Duration metaDataUpdateTime = Duration.ZERO;
    private @Nullable String resourceUri;
    private @Nullable String authenticationProfileUri;
    private BrokerTransportQualityOfService requestedDeliveryGuarantee =
        BrokerTransportQualityOfService.NotSpecified;

    private Builder() {}

    /**
     * Set the broker queue (e.g. MQTT topic) that NetworkMessages are sent to or received from.
     *
     * @param queueName the queue name.
     * @return this {@link Builder}.
     */
    public Builder queueName(String queueName) {
      this.queueName = queueName;
      return this;
    }

    /**
     * Set the broker queue that DataSetMetaData messages are sent to or received from.
     *
     * @param metaDataQueueName the metadata queue name.
     * @return this {@link Builder}.
     */
    public Builder metaDataQueueName(String metaDataQueueName) {
      this.metaDataQueueName = metaDataQueueName;
      return this;
    }

    /**
     * Set the interval at which DataSetMetaData is sent to the metadata queue.
     *
     * <p>A writer-level instance is authoritative for this value including its default of zero,
     * overriding any group-level update time; see {@link
     * BrokerTransportSettings#getMetaDataUpdateTime()}.
     *
     * @param metaDataUpdateTime the metadata update interval; {@link Duration#ZERO} (the default)
     *     means metadata is not sent cyclically.
     * @return this {@link Builder}.
     */
    public Builder metaDataUpdateTime(Duration metaDataUpdateTime) {
      this.metaDataUpdateTime = metaDataUpdateTime;
      return this;
    }

    /**
     * Set the resource URI used to look up broker connection settings.
     *
     * @param resourceUri the resource URI.
     * @return this {@link Builder}.
     */
    public Builder resourceUri(String resourceUri) {
      this.resourceUri = resourceUri;
      return this;
    }

    /**
     * Set the authentication profile URI used to look up broker credentials.
     *
     * @param authenticationProfileUri the authentication profile URI.
     * @return this {@link Builder}.
     */
    public Builder authenticationProfileUri(String authenticationProfileUri) {
      this.authenticationProfileUri = authenticationProfileUri;
      return this;
    }

    /**
     * Set the delivery guarantee (quality of service) requested from the broker.
     *
     * <p>A writer-level value is only valid when the writer also overrides {@code queueName} (Part
     * 14 §6.4.2.5.4): an enabled writer on a broker connection carrying a writer-level value
     * without one is rejected when the service starts or reconfigures; see {@link
     * BrokerTransportSettings#getRequestedDeliveryGuarantee()}.
     *
     * @param requestedDeliveryGuarantee the requested {@link BrokerTransportQualityOfService};
     *     defaults to {@link BrokerTransportQualityOfService#NotSpecified}.
     * @return this {@link Builder}.
     */
    public Builder requestedDeliveryGuarantee(
        BrokerTransportQualityOfService requestedDeliveryGuarantee) {
      this.requestedDeliveryGuarantee = requestedDeliveryGuarantee;
      return this;
    }

    /**
     * Build a new {@link BrokerTransportSettings} from the values configured on this builder.
     *
     * @return a new {@link BrokerTransportSettings}.
     * @throws PubSubConfigValidationException if {@code metaDataUpdateTime} is negative.
     */
    public BrokerTransportSettings build() {
      if (metaDataUpdateTime.isNegative()) {
        throw new PubSubConfigValidationException(
            "broker transport settings: metaDataUpdateTime must not be negative");
      }
      return new BrokerTransportSettings(this);
    }
  }
}
