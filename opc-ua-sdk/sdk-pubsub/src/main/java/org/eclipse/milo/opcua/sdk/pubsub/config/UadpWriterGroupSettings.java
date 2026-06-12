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

import java.util.Objects;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DataSetOrderingType;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;

/**
 * UADP message mapping settings for a {@link WriterGroupConfig}.
 *
 * <p>The default {@link UadpNetworkMessageContentMask} follows the Part 14 Annex A.2.2
 * "UADP-Dynamic" header layout profile, which is self-describing on the wire and the easiest layout
 * for third-party subscribers to consume.
 *
 * <p>SamplingOffset and PublishingOffset are not modeled; use {@code
 * WriterGroupConfig.Builder#rawMessageSettings(ExtensionObject)} if they are required.
 */
public final class UadpWriterGroupSettings implements WriterGroupMessageSettings {

  /**
   * The network message content mask of the Part 14 Annex A.2.2 "UADP-Dynamic" header layout
   * profile: PublisherId and PayloadHeader enabled.
   */
  public static final UadpNetworkMessageContentMask UADP_DYNAMIC_NETWORK_MESSAGE_CONTENT_MASK =
      UadpNetworkMessageContentMask.of(
          UadpNetworkMessageContentMask.Field.PublisherId,
          UadpNetworkMessageContentMask.Field.PayloadHeader);

  private final UInteger groupVersion;
  private final DataSetOrderingType dataSetOrdering;
  private final UadpNetworkMessageContentMask networkMessageContentMask;

  private UadpWriterGroupSettings(Builder builder) {
    this.groupVersion = builder.groupVersion;
    this.dataSetOrdering = builder.dataSetOrdering;
    this.networkMessageContentMask = builder.networkMessageContentMask;
  }

  /**
   * Get the GroupVersion: the VersionTime of the last layout change for the group's
   * NetworkMessages.
   *
   * @return the configured GroupVersion; 0 means the version is derived at component startup.
   */
  public UInteger getGroupVersion() {
    return groupVersion;
  }

  /**
   * Get the ordering of DataSetMessages in the group's NetworkMessages.
   *
   * @return the configured {@link DataSetOrderingType}.
   */
  public DataSetOrderingType getDataSetOrdering() {
    return dataSetOrdering;
  }

  /**
   * Get the mask selecting the optional UADP NetworkMessage header fields.
   *
   * @return the configured {@link UadpNetworkMessageContentMask}.
   */
  public UadpNetworkMessageContentMask getNetworkMessageContentMask() {
    return networkMessageContentMask;
  }

  /**
   * Create a new {@link Builder} initialized with the values of this instance.
   *
   * @return a new {@link Builder}.
   */
  public Builder toBuilder() {
    Builder builder = new Builder();
    builder.groupVersion = groupVersion;
    builder.dataSetOrdering = dataSetOrdering;
    builder.networkMessageContentMask = networkMessageContentMask;
    return builder;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof UadpWriterGroupSettings that)) {
      return false;
    }
    return groupVersion.equals(that.groupVersion)
        && dataSetOrdering == that.dataSetOrdering
        && networkMessageContentMask.equals(that.networkMessageContentMask);
  }

  @Override
  public int hashCode() {
    return Objects.hash(groupVersion, dataSetOrdering, networkMessageContentMask);
  }

  /**
   * Create a new {@link Builder} with default values.
   *
   * @return a new {@link Builder}.
   */
  public static Builder builder() {
    return new Builder();
  }

  /** A builder of {@link UadpWriterGroupSettings} instances. */
  public static final class Builder {

    private UInteger groupVersion = uint(0);
    private DataSetOrderingType dataSetOrdering = DataSetOrderingType.Undefined;
    private UadpNetworkMessageContentMask networkMessageContentMask =
        UADP_DYNAMIC_NETWORK_MESSAGE_CONTENT_MASK;

    private Builder() {}

    /**
     * Set the GroupVersion.
     *
     * @param groupVersion the GroupVersion; 0 (the default) means the version is derived at
     *     component startup.
     * @return this {@link Builder}.
     */
    public Builder groupVersion(UInteger groupVersion) {
      this.groupVersion = groupVersion;
      return this;
    }

    /**
     * Set the ordering of DataSetMessages in the group's NetworkMessages.
     *
     * @param dataSetOrdering the {@link DataSetOrderingType}; defaults to {@link
     *     DataSetOrderingType#Undefined}.
     * @return this {@link Builder}.
     */
    public Builder dataSetOrdering(DataSetOrderingType dataSetOrdering) {
      this.dataSetOrdering = dataSetOrdering;
      return this;
    }

    /**
     * Set the mask selecting the optional UADP NetworkMessage header fields.
     *
     * @param networkMessageContentMask the {@link UadpNetworkMessageContentMask}; defaults to
     *     {@link #UADP_DYNAMIC_NETWORK_MESSAGE_CONTENT_MASK}.
     * @return this {@link Builder}.
     */
    public Builder networkMessageContentMask(
        UadpNetworkMessageContentMask networkMessageContentMask) {
      this.networkMessageContentMask = networkMessageContentMask;
      return this;
    }

    /**
     * Build a new {@link UadpWriterGroupSettings} from the values configured on this builder.
     *
     * @return a new {@link UadpWriterGroupSettings}.
     */
    public UadpWriterGroupSettings build() {
      return new UadpWriterGroupSettings(this);
    }
  }
}
