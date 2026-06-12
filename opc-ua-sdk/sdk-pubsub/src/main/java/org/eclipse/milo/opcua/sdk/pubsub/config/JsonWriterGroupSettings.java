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

import org.eclipse.milo.opcua.stack.core.types.structured.JsonNetworkMessageContentMask;

/**
 * JSON message mapping settings for a {@link WriterGroupConfig}.
 *
 * <p>JSON settings are only valid on MQTT connections. An empty (default) content mask is not
 * emitted literally: the JSON mapping applies effective defaults ({@code NetworkMessageHeader},
 * {@code DataSetMessageHeader}, {@code PublisherId}), so a fully headerless network message cannot
 * be emitted; headerless layouts are still accepted on decode.
 */
public final class JsonWriterGroupSettings implements WriterGroupMessageSettings {

  private final JsonNetworkMessageContentMask networkMessageContentMask;

  private JsonWriterGroupSettings(Builder builder) {
    this.networkMessageContentMask = builder.networkMessageContentMask;
  }

  /**
   * Get the mask selecting the optional JSON NetworkMessage fields.
   *
   * @return the configured {@link JsonNetworkMessageContentMask}.
   */
  public JsonNetworkMessageContentMask getNetworkMessageContentMask() {
    return networkMessageContentMask;
  }

  /**
   * Create a new {@link Builder} initialized with the values of this instance.
   *
   * @return a new {@link Builder}.
   */
  public Builder toBuilder() {
    Builder builder = new Builder();
    builder.networkMessageContentMask = networkMessageContentMask;
    return builder;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof JsonWriterGroupSettings that)) {
      return false;
    }
    return networkMessageContentMask.equals(that.networkMessageContentMask);
  }

  @Override
  public int hashCode() {
    return networkMessageContentMask.hashCode();
  }

  /**
   * Create a new {@link Builder} with default values.
   *
   * @return a new {@link Builder}.
   */
  public static Builder builder() {
    return new Builder();
  }

  /** A builder of {@link JsonWriterGroupSettings} instances. */
  public static final class Builder {

    private JsonNetworkMessageContentMask networkMessageContentMask =
        JsonNetworkMessageContentMask.of();

    private Builder() {}

    /**
     * Set the mask selecting the optional JSON NetworkMessage fields.
     *
     * @param networkMessageContentMask the {@link JsonNetworkMessageContentMask}; defaults to an
     *     empty mask.
     * @return this {@link Builder}.
     */
    public Builder networkMessageContentMask(
        JsonNetworkMessageContentMask networkMessageContentMask) {
      this.networkMessageContentMask = networkMessageContentMask;
      return this;
    }

    /**
     * Build a new {@link JsonWriterGroupSettings} from the values configured on this builder.
     *
     * @return a new {@link JsonWriterGroupSettings}.
     */
    public JsonWriterGroupSettings build() {
      return new JsonWriterGroupSettings(this);
    }
  }
}
