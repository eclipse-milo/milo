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

import java.util.Objects;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonNetworkMessageContentMask;

/**
 * JSON message mapping settings for a {@link DataSetReaderConfig}.
 *
 * <p>JSON settings are only valid on MQTT connections. The JSON mapping decodes tolerantly: all
 * Part 14 network-message layouts (including headerless forms) and all field encodings are accepted
 * regardless of the masks configured here.
 */
public final class JsonDataSetReaderSettings implements DataSetReaderMessageSettings {

  private final JsonNetworkMessageContentMask networkMessageContentMask;
  private final JsonDataSetMessageContentMask dataSetMessageContentMask;

  private JsonDataSetReaderSettings(Builder builder) {
    this.networkMessageContentMask = builder.networkMessageContentMask;
    this.dataSetMessageContentMask = builder.dataSetMessageContentMask;
  }

  /**
   * Get the mask describing the optional JSON NetworkMessage fields the reader expects.
   *
   * @return the expected {@link JsonNetworkMessageContentMask}.
   */
  public JsonNetworkMessageContentMask getNetworkMessageContentMask() {
    return networkMessageContentMask;
  }

  /**
   * Get the mask describing the optional JSON DataSetMessage fields the reader expects.
   *
   * @return the expected {@link JsonDataSetMessageContentMask}.
   */
  public JsonDataSetMessageContentMask getDataSetMessageContentMask() {
    return dataSetMessageContentMask;
  }

  /**
   * Create a new {@link Builder} initialized with the values of this instance.
   *
   * @return a new {@link Builder}.
   */
  public Builder toBuilder() {
    Builder builder = new Builder();
    builder.networkMessageContentMask = networkMessageContentMask;
    builder.dataSetMessageContentMask = dataSetMessageContentMask;
    return builder;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof JsonDataSetReaderSettings that)) {
      return false;
    }
    return networkMessageContentMask.equals(that.networkMessageContentMask)
        && dataSetMessageContentMask.equals(that.dataSetMessageContentMask);
  }

  @Override
  public int hashCode() {
    return Objects.hash(networkMessageContentMask, dataSetMessageContentMask);
  }

  /**
   * Create a new {@link Builder} with default values.
   *
   * @return a new {@link Builder}.
   */
  public static Builder builder() {
    return new Builder();
  }

  /** A builder of {@link JsonDataSetReaderSettings} instances. */
  public static final class Builder {

    private JsonNetworkMessageContentMask networkMessageContentMask =
        JsonNetworkMessageContentMask.of();
    private JsonDataSetMessageContentMask dataSetMessageContentMask =
        JsonDataSetMessageContentMask.of();

    private Builder() {}

    /**
     * Set the mask describing the optional JSON NetworkMessage fields the reader expects.
     *
     * @param networkMessageContentMask the expected {@link JsonNetworkMessageContentMask}; defaults
     *     to an empty mask.
     * @return this {@link Builder}.
     */
    public Builder networkMessageContentMask(
        JsonNetworkMessageContentMask networkMessageContentMask) {
      this.networkMessageContentMask = networkMessageContentMask;
      return this;
    }

    /**
     * Set the mask describing the optional JSON DataSetMessage fields the reader expects.
     *
     * @param dataSetMessageContentMask the expected {@link JsonDataSetMessageContentMask}; defaults
     *     to an empty mask.
     * @return this {@link Builder}.
     */
    public Builder dataSetMessageContentMask(
        JsonDataSetMessageContentMask dataSetMessageContentMask) {
      this.dataSetMessageContentMask = dataSetMessageContentMask;
      return this;
    }

    /**
     * Build a new {@link JsonDataSetReaderSettings} from the values configured on this builder.
     *
     * @return a new {@link JsonDataSetReaderSettings}.
     */
    public JsonDataSetReaderSettings build() {
      return new JsonDataSetReaderSettings(this);
    }
  }
}
