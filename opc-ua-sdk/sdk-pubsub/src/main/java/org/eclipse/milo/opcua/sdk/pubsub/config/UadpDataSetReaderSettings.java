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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;

import java.util.Objects;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;

/**
 * UADP message mapping settings for a {@link DataSetReaderConfig}, describing the headers the
 * reader expects on received NetworkMessages and DataSetMessages.
 *
 * <p>The default content masks follow the Part 14 Annex A.2.2 "UADP-Dynamic" header layout profile,
 * matching the defaults of {@link UadpWriterGroupSettings} and {@link UadpDataSetWriterSettings}.
 */
public final class UadpDataSetReaderSettings implements DataSetReaderMessageSettings {

  private final UInteger groupVersion;
  private final UShort networkMessageNumber;
  private final UShort dataSetOffset;
  private final UadpNetworkMessageContentMask networkMessageContentMask;
  private final UadpDataSetMessageContentMask dataSetMessageContentMask;

  private UadpDataSetReaderSettings(Builder builder) {
    this.groupVersion = builder.groupVersion;
    this.networkMessageNumber = builder.networkMessageNumber;
    this.dataSetOffset = builder.dataSetOffset;
    this.networkMessageContentMask = builder.networkMessageContentMask;
    this.dataSetMessageContentMask = builder.dataSetMessageContentMask;
  }

  /**
   * Get the GroupVersion the reader expects in received GroupHeaders.
   *
   * @return the expected GroupVersion; 0 means the GroupVersion is not checked.
   */
  public UInteger getGroupVersion() {
    return groupVersion;
  }

  /**
   * Get the number of the NetworkMessage inside a publishing interval that carries the
   * DataSetMessage of interest, for fixed layouts.
   *
   * @return the NetworkMessage number; 0 means not configured.
   */
  public UShort getNetworkMessageNumber() {
    return networkMessageNumber;
  }

  /**
   * Get the offset of the DataSetMessage of interest inside the NetworkMessage, for fixed layouts.
   *
   * @return the offset in bytes; 0 means not configured.
   */
  public UShort getDataSetOffset() {
    return dataSetOffset;
  }

  /**
   * Get the mask describing the optional UADP NetworkMessage header fields the reader expects.
   *
   * @return the expected {@link UadpNetworkMessageContentMask}.
   */
  public UadpNetworkMessageContentMask getNetworkMessageContentMask() {
    return networkMessageContentMask;
  }

  /**
   * Get the mask describing the optional UADP DataSetMessage header fields the reader expects.
   *
   * @return the expected {@link UadpDataSetMessageContentMask}.
   */
  public UadpDataSetMessageContentMask getDataSetMessageContentMask() {
    return dataSetMessageContentMask;
  }

  /**
   * Create a new {@link Builder} initialized with the values of this instance.
   *
   * @return a new {@link Builder}.
   */
  public Builder toBuilder() {
    Builder builder = new Builder();
    builder.groupVersion = groupVersion;
    builder.networkMessageNumber = networkMessageNumber;
    builder.dataSetOffset = dataSetOffset;
    builder.networkMessageContentMask = networkMessageContentMask;
    builder.dataSetMessageContentMask = dataSetMessageContentMask;
    return builder;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof UadpDataSetReaderSettings that)) {
      return false;
    }
    return groupVersion.equals(that.groupVersion)
        && networkMessageNumber.equals(that.networkMessageNumber)
        && dataSetOffset.equals(that.dataSetOffset)
        && networkMessageContentMask.equals(that.networkMessageContentMask)
        && dataSetMessageContentMask.equals(that.dataSetMessageContentMask);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        groupVersion,
        networkMessageNumber,
        dataSetOffset,
        networkMessageContentMask,
        dataSetMessageContentMask);
  }

  /**
   * Create a new {@link Builder} with default values.
   *
   * @return a new {@link Builder}.
   */
  public static Builder builder() {
    return new Builder();
  }

  /** A builder of {@link UadpDataSetReaderSettings} instances. */
  public static final class Builder {

    private UInteger groupVersion = uint(0);
    private UShort networkMessageNumber = ushort(0);
    private UShort dataSetOffset = ushort(0);
    private UadpNetworkMessageContentMask networkMessageContentMask =
        UadpWriterGroupSettings.UADP_DYNAMIC_NETWORK_MESSAGE_CONTENT_MASK;
    private UadpDataSetMessageContentMask dataSetMessageContentMask =
        UadpDataSetWriterSettings.UADP_DYNAMIC_DATA_SET_MESSAGE_CONTENT_MASK;

    private Builder() {}

    /**
     * Set the GroupVersion the reader expects in received GroupHeaders.
     *
     * @param groupVersion the expected GroupVersion; 0 (the default) means the GroupVersion is not
     *     checked.
     * @return this {@link Builder}.
     */
    public Builder groupVersion(UInteger groupVersion) {
      this.groupVersion = groupVersion;
      return this;
    }

    /**
     * Set the number of the NetworkMessage inside a publishing interval that carries the
     * DataSetMessage of interest, for fixed layouts.
     *
     * @param networkMessageNumber the NetworkMessage number; 0 (the default) means not configured.
     * @return this {@link Builder}.
     */
    public Builder networkMessageNumber(UShort networkMessageNumber) {
      this.networkMessageNumber = networkMessageNumber;
      return this;
    }

    /**
     * Set the offset of the DataSetMessage of interest inside the NetworkMessage, for fixed
     * layouts.
     *
     * @param dataSetOffset the offset in bytes; 0 (the default) means not configured.
     * @return this {@link Builder}.
     */
    public Builder dataSetOffset(UShort dataSetOffset) {
      this.dataSetOffset = dataSetOffset;
      return this;
    }

    /**
     * Set the mask describing the optional UADP NetworkMessage header fields the reader expects.
     *
     * @param networkMessageContentMask the expected {@link UadpNetworkMessageContentMask}; defaults
     *     to {@link UadpWriterGroupSettings#UADP_DYNAMIC_NETWORK_MESSAGE_CONTENT_MASK}.
     * @return this {@link Builder}.
     */
    public Builder networkMessageContentMask(
        UadpNetworkMessageContentMask networkMessageContentMask) {
      this.networkMessageContentMask = networkMessageContentMask;
      return this;
    }

    /**
     * Set the mask describing the optional UADP DataSetMessage header fields the reader expects.
     *
     * @param dataSetMessageContentMask the expected {@link UadpDataSetMessageContentMask}; defaults
     *     to {@link UadpDataSetWriterSettings#UADP_DYNAMIC_DATA_SET_MESSAGE_CONTENT_MASK}.
     * @return this {@link Builder}.
     */
    public Builder dataSetMessageContentMask(
        UadpDataSetMessageContentMask dataSetMessageContentMask) {
      this.dataSetMessageContentMask = dataSetMessageContentMask;
      return this;
    }

    /**
     * Build a new {@link UadpDataSetReaderSettings} from the values configured on this builder.
     *
     * @return a new {@link UadpDataSetReaderSettings}.
     */
    public UadpDataSetReaderSettings build() {
      return new UadpDataSetReaderSettings(this);
    }
  }
}
