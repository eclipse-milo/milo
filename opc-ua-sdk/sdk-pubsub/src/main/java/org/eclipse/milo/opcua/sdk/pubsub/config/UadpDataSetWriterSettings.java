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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;

import java.util.Objects;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;

/**
 * UADP message mapping settings for a {@link DataSetWriterConfig}.
 *
 * <p>The default {@link UadpDataSetMessageContentMask} follows the Part 14 Annex A.2.2
 * "UADP-Dynamic" header layout profile.
 */
public final class UadpDataSetWriterSettings implements DataSetWriterMessageSettings {

  /**
   * The DataSetMessage content mask of the Part 14 Annex A.2.2 "UADP-Dynamic" header layout
   * profile: Timestamp, Status, MinorVersion, and SequenceNumber enabled.
   */
  public static final UadpDataSetMessageContentMask UADP_DYNAMIC_DATA_SET_MESSAGE_CONTENT_MASK =
      UadpDataSetMessageContentMask.of(
          UadpDataSetMessageContentMask.Field.Timestamp,
          UadpDataSetMessageContentMask.Field.Status,
          UadpDataSetMessageContentMask.Field.MinorVersion,
          UadpDataSetMessageContentMask.Field.SequenceNumber);

  private final UadpDataSetMessageContentMask dataSetMessageContentMask;
  private final UShort configuredSize;
  private final UShort networkMessageNumber;
  private final UShort dataSetOffset;

  private UadpDataSetWriterSettings(Builder builder) {
    this.dataSetMessageContentMask = builder.dataSetMessageContentMask;
    this.configuredSize = builder.configuredSize;
    this.networkMessageNumber = builder.networkMessageNumber;
    this.dataSetOffset = builder.dataSetOffset;
  }

  /**
   * Get the mask selecting the optional UADP DataSetMessage header fields.
   *
   * @return the configured {@link UadpDataSetMessageContentMask}.
   */
  public UadpDataSetMessageContentMask getDataSetMessageContentMask() {
    return dataSetMessageContentMask;
  }

  /**
   * Get the fixed encoded size of the DataSetMessage.
   *
   * @return the configured size in bytes; 0 means the size is dynamic. A non-zero size cannot be
   *     combined with a {@code keyFrameCount} greater than 1: fixed-size layouts are key-frame-only
   *     (Part 14 Annex A.2.1.7), enforced at startup and reconfigure.
   */
  public UShort getConfiguredSize() {
    return configuredSize;
  }

  /**
   * Get the number of the NetworkMessage inside a publishing interval that this writer's
   * DataSetMessage is placed in, for fixed layouts.
   *
   * @return the NetworkMessage number; 0 means not configured.
   */
  public UShort getNetworkMessageNumber() {
    return networkMessageNumber;
  }

  /**
   * Get the offset of the DataSetMessage inside the NetworkMessage, for fixed layouts.
   *
   * @return the offset in bytes; 0 means not configured.
   */
  public UShort getDataSetOffset() {
    return dataSetOffset;
  }

  /**
   * Create a new {@link Builder} initialized with the values of this instance.
   *
   * @return a new {@link Builder}.
   */
  public Builder toBuilder() {
    Builder builder = new Builder();
    builder.dataSetMessageContentMask = dataSetMessageContentMask;
    builder.configuredSize = configuredSize;
    builder.networkMessageNumber = networkMessageNumber;
    builder.dataSetOffset = dataSetOffset;
    return builder;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof UadpDataSetWriterSettings that)) {
      return false;
    }
    return dataSetMessageContentMask.equals(that.dataSetMessageContentMask)
        && configuredSize.equals(that.configuredSize)
        && networkMessageNumber.equals(that.networkMessageNumber)
        && dataSetOffset.equals(that.dataSetOffset);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        dataSetMessageContentMask, configuredSize, networkMessageNumber, dataSetOffset);
  }

  /**
   * Create a new {@link Builder} with default values.
   *
   * @return a new {@link Builder}.
   */
  public static Builder builder() {
    return new Builder();
  }

  /** A builder of {@link UadpDataSetWriterSettings} instances. */
  public static final class Builder {

    private UadpDataSetMessageContentMask dataSetMessageContentMask =
        UADP_DYNAMIC_DATA_SET_MESSAGE_CONTENT_MASK;
    private UShort configuredSize = ushort(0);
    private UShort networkMessageNumber = ushort(0);
    private UShort dataSetOffset = ushort(0);

    private Builder() {}

    /**
     * Set the mask selecting the optional UADP DataSetMessage header fields.
     *
     * @param dataSetMessageContentMask the {@link UadpDataSetMessageContentMask}; defaults to
     *     {@link #UADP_DYNAMIC_DATA_SET_MESSAGE_CONTENT_MASK}.
     * @return this {@link Builder}.
     */
    public Builder dataSetMessageContentMask(
        UadpDataSetMessageContentMask dataSetMessageContentMask) {
      this.dataSetMessageContentMask = dataSetMessageContentMask;
      return this;
    }

    /**
     * Set the fixed encoded size of the DataSetMessage.
     *
     * @param configuredSize the size in bytes; 0 (the default) means the size is dynamic. A
     *     non-zero size cannot be combined with a {@code keyFrameCount} greater than 1: fixed-size
     *     layouts are key-frame-only (Part 14 Annex A.2.1.7), enforced at startup and reconfigure.
     * @return this {@link Builder}.
     */
    public Builder configuredSize(UShort configuredSize) {
      this.configuredSize = configuredSize;
      return this;
    }

    /**
     * Set the number of the NetworkMessage inside a publishing interval that this writer's
     * DataSetMessage is placed in, for fixed layouts.
     *
     * @param networkMessageNumber the NetworkMessage number; 0 (the default) means not configured.
     * @return this {@link Builder}.
     */
    public Builder networkMessageNumber(UShort networkMessageNumber) {
      this.networkMessageNumber = networkMessageNumber;
      return this;
    }

    /**
     * Set the offset of the DataSetMessage inside the NetworkMessage, for fixed layouts.
     *
     * @param dataSetOffset the offset in bytes; 0 (the default) means not configured.
     * @return this {@link Builder}.
     */
    public Builder dataSetOffset(UShort dataSetOffset) {
      this.dataSetOffset = dataSetOffset;
      return this;
    }

    /**
     * Build a new {@link UadpDataSetWriterSettings} from the values configured on this builder.
     *
     * @return a new {@link UadpDataSetWriterSettings}.
     */
    public UadpDataSetWriterSettings build() {
      return new UadpDataSetWriterSettings(this);
    }
  }
}
