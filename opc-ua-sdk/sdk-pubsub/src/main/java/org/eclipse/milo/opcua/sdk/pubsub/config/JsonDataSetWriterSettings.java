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

import org.eclipse.milo.opcua.stack.core.types.structured.JsonDataSetMessageContentMask;

/**
 * JSON message mapping settings for a {@link DataSetWriterConfig}.
 *
 * <p>JSON settings are only valid on MQTT connections. An empty (default) content mask is not
 * emitted literally: the JSON mapping applies effective defaults ({@code DataSetWriterId}, {@code
 * SequenceNumber}, {@code MetaDataVersion}, {@code Timestamp}, {@code Status}, {@code
 * FieldEncoding2} = Verbose). Deprecated field-encoding combinations (Reversible/NonReversible) are
 * upgraded to their modern equivalents on emission.
 */
public final class JsonDataSetWriterSettings implements DataSetWriterMessageSettings {

  private final JsonDataSetMessageContentMask dataSetMessageContentMask;

  private JsonDataSetWriterSettings(Builder builder) {
    this.dataSetMessageContentMask = builder.dataSetMessageContentMask;
  }

  /**
   * Get the mask selecting the optional JSON DataSetMessage fields.
   *
   * @return the configured {@link JsonDataSetMessageContentMask}.
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
    builder.dataSetMessageContentMask = dataSetMessageContentMask;
    return builder;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof JsonDataSetWriterSettings that)) {
      return false;
    }
    return dataSetMessageContentMask.equals(that.dataSetMessageContentMask);
  }

  @Override
  public int hashCode() {
    return dataSetMessageContentMask.hashCode();
  }

  /**
   * Create a new {@link Builder} with default values.
   *
   * @return a new {@link Builder}.
   */
  public static Builder builder() {
    return new Builder();
  }

  /** A builder of {@link JsonDataSetWriterSettings} instances. */
  public static final class Builder {

    private JsonDataSetMessageContentMask dataSetMessageContentMask =
        JsonDataSetMessageContentMask.of();

    private Builder() {}

    /**
     * Set the mask selecting the optional JSON DataSetMessage fields.
     *
     * @param dataSetMessageContentMask the {@link JsonDataSetMessageContentMask}; defaults to an
     *     empty mask.
     * @return this {@link Builder}.
     */
    public Builder dataSetMessageContentMask(
        JsonDataSetMessageContentMask dataSetMessageContentMask) {
      this.dataSetMessageContentMask = dataSetMessageContentMask;
      return this;
    }

    /**
     * Build a new {@link JsonDataSetWriterSettings} from the values configured on this builder.
     *
     * @return a new {@link JsonDataSetWriterSettings}.
     */
    public JsonDataSetWriterSettings build() {
      return new JsonDataSetWriterSettings(this);
    }
  }
}
