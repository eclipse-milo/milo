/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.objects;

import java.util.UUID;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.2.1/#9.2.1.3">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.2.1/#9.2.1.3</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface UadpDataSetReaderMessageType extends DataSetReaderMessageType {
  QualifiedProperty<UInteger> GROUP_VERSION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "GroupVersion",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=20998"),
          -1,
          UInteger.class);

  QualifiedProperty<UShort> NETWORK_MESSAGE_NUMBER =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "NetworkMessageNumber",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  QualifiedProperty<UShort> DATA_SET_OFFSET =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DataSetOffset",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  QualifiedProperty<UUID> DATA_SET_CLASS_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DataSetClassId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=14"),
          -1,
          UUID.class);

  QualifiedProperty<UadpNetworkMessageContentMask> NETWORK_MESSAGE_CONTENT_MASK =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "NetworkMessageContentMask",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15642"),
          -1,
          UadpNetworkMessageContentMask.class);

  QualifiedProperty<UadpDataSetMessageContentMask> DATA_SET_MESSAGE_CONTENT_MASK =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DataSetMessageContentMask",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15646"),
          -1,
          UadpDataSetMessageContentMask.class);

  QualifiedProperty<Double> PUBLISHING_INTERVAL =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "PublishingInterval",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<Double> PROCESSING_OFFSET =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ProcessingOffset",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<Double> RECEIVE_OFFSET =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ReceiveOffset",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  /** Gets the existing node's local value. */
  @Nullable UInteger getGroupVersion();

  /** Sets the existing node's local value. */
  void setGroupVersion(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getGroupVersionNode();

  /** Gets the existing node's local value. */
  @Nullable UShort getNetworkMessageNumber();

  /** Sets the existing node's local value. */
  void setNetworkMessageNumber(@Nullable UShort value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getNetworkMessageNumberNode();

  /** Gets the existing node's local value. */
  @Nullable UShort getDataSetOffset();

  /** Sets the existing node's local value. */
  void setDataSetOffset(@Nullable UShort value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getDataSetOffsetNode();

  /** Gets the existing node's local value. */
  @Nullable UUID getDataSetClassId();

  /** Sets the existing node's local value. */
  void setDataSetClassId(@Nullable UUID value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getDataSetClassIdNode();

  /** Gets the existing node's local value. */
  @Nullable UadpNetworkMessageContentMask getNetworkMessageContentMask();

  /** Sets the existing node's local value. */
  void setNetworkMessageContentMask(@Nullable UadpNetworkMessageContentMask value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getNetworkMessageContentMaskNode();

  /** Gets the existing node's local value. */
  @Nullable UadpDataSetMessageContentMask getDataSetMessageContentMask();

  /** Sets the existing node's local value. */
  void setDataSetMessageContentMask(@Nullable UadpDataSetMessageContentMask value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getDataSetMessageContentMaskNode();

  /** Gets the existing node's local value. */
  @Nullable Double getPublishingInterval();

  /** Sets the existing node's local value. */
  void setPublishingInterval(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getPublishingIntervalNode();

  /** Gets the existing node's local value. */
  @Nullable Double getProcessingOffset();

  /** Sets the existing node's local value. */
  void setProcessingOffset(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getProcessingOffsetNode();

  /** Gets the existing node's local value. */
  @Nullable Double getReceiveOffset();

  /** Sets the existing node's local value. */
  void setReceiveOffset(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getReceiveOffsetNode();
}
